pipeline {
    agent any

    tools {
        maven 'Maven-3.9.9'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Code checked out from GitHub'
            }
        }

        stage('Build Product Service') {
            steps {
                sh '''
                cd product-service
                mvn clean package -DskipTests
                '''
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

    }
}
