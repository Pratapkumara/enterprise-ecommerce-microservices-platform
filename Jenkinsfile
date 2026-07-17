pipeline {

    agent any

    tools {
        maven 'Maven-3.9.9'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Code checkout from GitHub successful'
            }
        }

        stage('Build Product Service') {
            steps {
                echo 'Building Product Service'

                sh '''
                cd product-service
                mvn clean package -DskipTests
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {

                echo 'Running SonarQube Analysis'

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

                echo 'Checking SonarQube Quality Gate'

                timeout(time: 5, unit: 'MINUTES') {

                    waitForQualityGate abortPipeline: true

                }

            }
        }

    }


    post {

        success {
            echo 'Pipeline completed successfully ✅'
        }

        failure {
            echo 'Pipeline failed ❌'
        }

    }

}
