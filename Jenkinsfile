pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(
            numToKeepStr: '10',
            artifactNumToKeepStr: '5'
        ))
        timeout(time: 90, unit: 'MINUTES')
    }

    environment {
        SERVICES = 'config-server discovery-server api-gateway user-service product-service inventory-service order-service payment-service notification-service'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        GIT_REPOSITORY = 'github.com/Pratapkumara/enterprise-ecommerce-microservices-platform.git'
        ARGO_APPLICATION = 'ecommerce-platform'
        ARGO_NAMESPACE = 'argocd'
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                checkout scm

                sh '''
                    set -eu

                    echo "Commit: $(git rev-parse --short HEAD)"
                    echo "Branch: $(git branch --show-current)"
                    echo "Image tag: ${IMAGE_TAG}"
                '''
            }
        }

        stage('Validate Tools') {
            steps {
                sh '''
                    set -eu

                    java -version
                    mvn -version
                    docker version --format 'Docker {{.Client.Version}}'
                    trivy --version
                    kubectl version --client
                    helm version --short

                    docker ps >/dev/null
                    kubectl get nodes
                    curl -fsS http://sonarqube:9000/api/system/status
                '''
            }
        }

        stage('Build and Test') {
            steps {
                sh '''
                    set -eu

                    for service in ${SERVICES}; do
                        echo "========================================"
                        echo "Building and testing ${service}"
                        echo "========================================"

                        mvn \
                          -B \
                          -ntp \
                          -f "${service}/pom.xml" \
                          clean verify
                    done
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def services = env.SERVICES.tokenize()

                    for (String service : services) {
                        withEnv(["CURRENT_SERVICE=${service}"]) {
                            withSonarQubeEnv('sonarqube') {
                                sh '''
                                    set -eu

                                    echo "Running SonarQube analysis for ${CURRENT_SERVICE}"

                                    mvn \
                                      -B \
                                      -ntp \
                                      -f "${CURRENT_SERVICE}/pom.xml" \
                                      sonar:sonar \
                                      -Dsonar.projectKey="enterprise-ecommerce-${CURRENT_SERVICE}" \
                                      -Dsonar.projectName="Enterprise E-commerce - ${CURRENT_SERVICE}" \
                                      -Dsonar.token="${SONAR_AUTH_TOKEN}"
                                '''
                            }

                            timeout(time: 10, unit: 'MINUTES') {
                                waitForQualityGate abortPipeline: true
                            }
                        }
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh '''
                    set -eu

                    for service in ${SERVICES}; do
                        image="${service}:${IMAGE_TAG}"

                        echo "========================================"
                        echo "Building ${image}"
                        echo "========================================"

                        docker build \
                          --pull \
                          --tag "${image}" \
                          "${service}"
                    done
                '''
            }
        }

        stage('Trivy Security Scan') {
            steps {
                sh '''
                    set -eu

                    mkdir -p trivy-reports

                    for service in ${SERVICES}; do
                        image="${service}:${IMAGE_TAG}"
                        report="trivy-reports/${service}.txt"

                        echo "========================================"
                        echo "Scanning ${image}"
                        echo "========================================"

                        trivy image \
                          --scanners vuln \
                          --ignore-unfixed \
                          --severity CRITICAL \
                          --exit-code 1 \
                          --no-progress \
                          --format table \
                          --output "${report}" \
                          "${image}"
                    done
                '''
            }

            post {
                always {
                    archiveArtifacts(
                        artifacts: 'trivy-reports/*.txt',
                        allowEmptyArchive: true
                    )
                }
            }
        }

        stage('Import Images into Minikube') {
            steps {
                sh '''
                    set -eu

                    for service in ${SERVICES}; do
                        image="${service}:${IMAGE_TAG}"

                        echo "========================================"
                        echo "Importing ${image} into Minikube"
                        echo "========================================"

                        docker save "${image}" |
                        docker exec -i minikube \
                          ctr -n k8s.io images import -
                    done
                '''
            }
        }

        stage('Update Helm Image Tags') {
            steps {
                sh '''
                    set -eu

                    values_file="helm/ecommerce/values.yaml"

                    for service in ${SERVICES}; do
                        sed -i \
                          "/repository: ${service}/{n;s/tag: .*/tag: \\"${IMAGE_TAG}\\"/;}" \
                          "${values_file}"
                    done

                    echo "Updated image tags:"
                    grep -A1 'repository:' "${values_file}"
                '''
            }
        }

        stage('Validate Helm Chart') {
            steps {
                sh '''
                    set -eu

                    helm lint helm/ecommerce
                    helm template ecommerce helm/ecommerce \
                      > /tmp/ecommerce-rendered.yaml

                    for service in ${SERVICES}; do
                        grep -q \
                          "image: \\"${service}:${IMAGE_TAG}\\"" \
                          /tmp/ecommerce-rendered.yaml
                    done

                    echo "All rendered images use ${IMAGE_TAG}"
                '''
            }
        }

        stage('Push GitOps Update') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'github-token',
                        variable: 'GITHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        set -eu

                        git config user.name "Jenkins CI"
                        git config user.email "jenkins@enterprise-ecommerce.local"

                        git add helm/ecommerce/values.yaml

                        if git diff --cached --quiet; then
                            echo "No Helm tag changes to commit"
                            exit 0
                        fi

                        git commit -m \
                          "chore: deploy ${IMAGE_TAG} [skip ci]"

                        set +x
                        git push \
                          "https://x-access-token:${GITHUB_TOKEN}@${GIT_REPOSITORY}" \
                          HEAD:main
                    '''
                }
            }
        }

        stage('Argo CD Deployment') {
            steps {
                sh '''
                    set -eu

                    kubectl annotate application \
                      "${ARGO_APPLICATION}" \
                      -n "${ARGO_NAMESPACE}" \
                      argocd.argoproj.io/refresh=hard \
                      --overwrite

                    echo "Waiting for Argo CD to deploy ${IMAGE_TAG}..."

                    deployed=false

                    for attempt in $(seq 1 60); do
                        all_updated=true

                        for service in ${SERVICES}; do
                            actual_image=$(
                                kubectl get deployment "${service}" \
                                  -o jsonpath='{.spec.template.spec.containers[0].image}'
                            )

                            if [ "${actual_image}" != "${service}:${IMAGE_TAG}" ]; then
                                all_updated=false
                                break
                            fi
                        done

                        if [ "${all_updated}" = "true" ]; then
                            deployed=true
                            break
                        fi

                        echo "Attempt ${attempt}/60: waiting for new images..."
                        sleep 10
                    done

                    if [ "${deployed}" != "true" ]; then
                        echo "Argo CD did not apply ${IMAGE_TAG} in time"
                        exit 1
                    fi

                    for service in ${SERVICES}; do
                        kubectl rollout status \
                          deployment/"${service}" \
                          --timeout=300s
                    done

                    sync_status=$(
                        kubectl get application "${ARGO_APPLICATION}" \
                          -n "${ARGO_NAMESPACE}" \
                          -o jsonpath='{.status.sync.status}'
                    )

                    health_status=$(
                        kubectl get application "${ARGO_APPLICATION}" \
                          -n "${ARGO_NAMESPACE}" \
                          -o jsonpath='{.status.health.status}'
                    )

                    echo "Argo CD sync: ${sync_status}"
                    echo "Argo CD health: ${health_status}"

                    test "${sync_status}" = "Synced"
                    test "${health_status}" = "Healthy"
                '''
            }
        }

        stage('Smoke Test') {
            steps {
                sh '''
                    set -eu

                    for service_port in \
                      "config-server:8888" \
                      "discovery-server:8761" \
                      "api-gateway:8082" \
                      "user-service:8083" \
                      "product-service:8084" \
                      "inventory-service:8086" \
                      "order-service:8087" \
                      "payment-service:8088" \
                      "notification-service:8089"
                    do
                        service="${service_port%:*}"
                        port="${service_port#*:}"

                        echo "Checking ${service}..."

                        kubectl exec deployment/"${service}" -- \
                          curl -fsS \
                          "http://localhost:${port}/actuator/health" \
                          >/dev/null
                    done

                    echo "All service health checks passed"
                '''
            }
        }
    }

    post {
        success {
            echo "CI/CD successful: ${IMAGE_TAG} deployed"
        }

        failure {
            echo 'CI/CD pipeline failed. Check the failed stage logs.'
        }

        always {
            junit(
                testResults: '**/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            cleanWs(
                deleteDirs: true,
                disableDeferredWipeout: true
            )
        }
    }
}
