pipeline {
    agent any 

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/chedlikh/kaddem.git' // Replace with your repository URL
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

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t kaddem .' // Build Docker image
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker run -d -p 8080:8080 kaddem' // Run Docker container
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
