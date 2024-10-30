pipeline {
    agent any 

    triggers {
        // Configure le déclenchement basé sur des push Git
        pollSCM('H/5 * * * *') // Vérifie les changements toutes les 5 minutes
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération du code depuis le référentiel Git
                git url: 'https://github.com/chedlikh/kaddem.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package' // For a Maven project
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test' // Run your tests
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageName = 'chedli1/kaddem' // Replace with your Docker Hub username and repository name
                    def imageTag = 'latest' // You can modify this to use a different tag if needed

                    // Build the Docker image
                    sh "docker build -t ${imageName}:${imageTag} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    def imageName = 'chedli1/kaddem' // Replace with your Docker Hub username and repository name
                    def imageTag = 'latest' // Ensure this matches the tag used during the build

                    // Push the Docker image to Docker Hub
                    sh "docker push ${imageName}:${imageTag}"
                }
            }
        }
    }
    
    post {
        always {
            // Optionnel : Nettoyage ou notifications
            echo 'Fin du pipeline.'
        }
    }
}
