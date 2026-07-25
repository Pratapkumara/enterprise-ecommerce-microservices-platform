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
        JACOCO_VERSION = '0.8.13'
        SONAR_MAVEN_PLUGIN_VERSION = '5.7.0.6970'
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
                    echo "Branch: ${BRANCH_NAME:-detached}"
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
                    docker version \
                      --format 'Docker {{.Client.Version}}'
                    trivy --version
                    kubectl version --client
                    helm version --short

                    docker ps >/dev/null
                    kubectl get nodes

                    docker inspect minikube \
                      --format='{{.State.Running}}' |
                      grep -q true

                    docker exec minikube \
                      docker info >/dev/null

                    curl -fsS \
                      http://sonarqube:9000/api/system/status
                '''
            }
        }

        stage('Build Test and Coverage') {
            steps {
                sh '''
                    set -eu

                    for service in ${SERVICES}; do
                        echo "========================================"
                        echo "Building, testing and covering ${service}"
                        echo "========================================"

                        mvn \
                          -B \
                          -ntp \
                          -f "${service}/pom.xml" \
                          clean \
                          "org.jacoco:jacoco-maven-plugin:${JACOCO_VERSION}:prepare-agent" \
                          verify \
                          "org.jacoco:jacoco-maven-plugin:${JACOCO_VERSION}:report"

                        report="${service}/target/site/jacoco/jacoco.xml"

                        if [ -f "${report}" ]; then
                            echo "JaCoCo report generated: ${report}"
                        else
                            echo "No JaCoCo report for ${service}; no tests may exist"
                        fi
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

                            sh '''
                                set -eu

                                find . \
                                  -type f \
                                  -path "*/target/sonar/report-task.txt" \
                                  -delete

                                echo "Cleared previous Sonar task metadata"
                            '''

                            withSonarQubeEnv('sonarqube') {
                                sh '''
                                    set -eu

                                    echo "Running SonarQube analysis for ${CURRENT_SERVICE}"

                                    mvn \
                                      -B \
                                      -ntp \
                                      -f "${CURRENT_SERVICE}/pom.xml" \
                                      "org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_MAVEN_PLUGIN_VERSION}:sonar" \
                                      -Dsonar.projectKey="enterprise-ecommerce-${CURRENT_SERVICE}" \
                                      -Dsonar.projectName="Enterprise E-commerce - ${CURRENT_SERVICE}" \
                                      -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                                '''
                            }

                            timeout(
                                time: 10,
                                unit: 'MINUTES'
                            ) {
                                def qualityGate =
                                    waitForQualityGate(
                                        abortPipeline: true
                                    )

                                echo(
                                    "${service} Quality Gate: " +
                                    "${qualityGate.status}"
                                )
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
                        echo "Importing ${image} into Minikube Docker"
                        echo "========================================"

                        docker save "${image}" |
                          docker exec -i minikube docker load

                        docker image rm "${image}"
                    done

                    echo "Verifying imported images..."

                    for service in ${SERVICES}; do
                        docker exec minikube \
                          docker image inspect \
                          "${service}:${IMAGE_TAG}" \
                          >/dev/null
                    done

                    echo "All ${IMAGE_TAG} images imported successfully"
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

                    rendered_file="$WORKSPACE/ecommerce-rendered.yaml"

                    helm lint helm/ecommerce

                    helm template ecommerce helm/ecommerce \
                      > "${rendered_file}"

                    for service in ${SERVICES}; do
                        if ! grep -q \
                          "image: \\"${service}:${IMAGE_TAG}\\"" \
                          "${rendered_file}"; then

                            echo "ERROR: ${service}:${IMAGE_TAG} was not found"
                            exit 1
                        fi
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
                        git config user.email \
                          "jenkins@enterprise-ecommerce.local"

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

                        echo "Attempt ${attempt}/60: waiting for images..."
                        sleep 10
                    done

                    if [ "${deployed}" != "true" ]; then
                        echo "Argo CD did not apply ${IMAGE_TAG} in time"
                        exit 1
                    fi

                    for service in ${SERVICES}; do
                        echo "Waiting for ${service} rollout..."

                        kubectl rollout status \
                          deployment/"${service}" \
                          --timeout=300s
                    done

                    application_healthy=false

                    for attempt in $(seq 1 30); do
                        sync_status=$(
                            kubectl get application \
                              "${ARGO_APPLICATION}" \
                              -n "${ARGO_NAMESPACE}" \
                              -o jsonpath='{.status.sync.status}'
                        )

                        health_status=$(
                            kubectl get application \
                              "${ARGO_APPLICATION}" \
                              -n "${ARGO_NAMESPACE}" \
                              -o jsonpath='{.status.health.status}'
                        )

                        echo "Argo CD sync: ${sync_status}"
                        echo "Argo CD health: ${health_status}"

                        if [ "${sync_status}" = "Synced" ] &&
                           [ "${health_status}" = "Healthy" ]; then

                            application_healthy=true
                            break
                        fi

                        echo "Waiting for Argo CD health..."
                        sleep 10
                    done

                    if [ "${application_healthy}" != "true" ]; then
                        echo "Argo CD application did not become healthy"
                        exit 1
                    fi
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
                        healthy=false

                        echo "========================================"
                        echo "Checking ${service}:${port}"
                        echo "========================================"

                        for attempt in $(seq 1 30); do
                            if kubectl exec \
                              deployment/"${service}" -- \
                              curl -fsS \
                              "http://localhost:${port}/actuator/health" \
                              >/dev/null 2>&1; then

                                echo "${service} is healthy"
                                healthy=true
                                break
                            fi

                            echo "Attempt ${attempt}/30: ${service} is not ready"
                            sleep 10
                        done

                        if [ "${healthy}" != "true" ]; then
                            echo "ERROR: ${service} failed its health check"

                            kubectl get pods \
                              -l "app=${service}" \
                              -o wide || true

                            kubectl logs \
                              deployment/"${service}" \
                              --tail=100 || true

                            exit 1
                        fi
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
            echo(
                'CI/CD pipeline failed. ' +
                'Check the failed stage logs.'
            )
        }

        always {
            junit(
                testResults: '**/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )

            archiveArtifacts(
                artifacts: '**/target/site/jacoco/jacoco.xml',
                allowEmptyArchive: true
            )

            sh '''
                set +e

                echo "Cleaning host images for ${IMAGE_TAG}..."

                for service in ${SERVICES}; do
                    docker image rm \
                      "${service}:${IMAGE_TAG}" \
                      >/dev/null 2>&1 || true
                done

                docker image prune -f \
                  >/dev/null 2>&1 || true
            '''

            cleanWs(
                deleteDirs: true,
                disableDeferredWipeout: true
            )
        }
    }
}
