pipeline {

    agent any

    tools {
        maven 'Maven-3.9.9'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Code checkout from GitHub'
            }
        }


        stage('Build All Microservices') {
            steps {

                script {

                    def services = [
                        'config-server',
                        'discovery-server',
                        'api-gateway',
                        'user-service',
                        'product-service',
                        'inventory-service',
                        'order-service',
                        'payment-service',
                        'notification-service'
                    ]


                    for (service in services) {

                        echo "Building ${service}"

                        sh """
                        cd ${service}
                        mvn clean package -DskipTests
                        cd ..
                        """

                    }

                }

            }
        }


        stage('SonarQube Analysis') {
            steps {

                withSonarQubeEnv('sonarqube') {

                    sh '''
                    cd product-service
                    mvn sonar:sonar
                    '''

                }

            }
        }


        stage('Quality Gate') {

            steps {

                timeout(time: 5, unit: 'MINUTES') {

                    waitForQualityGate abortPipeline: true

                }

            }

        }

    }


    post {

        success {
            echo 'All services build successful ✅'
        }

        failure {
            echo 'Pipeline failed ❌'
        }

    }

}
