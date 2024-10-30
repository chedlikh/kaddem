pipeline {
    agent { label 'contrôleur' }

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
         stage('Build Docker Image') {
            steps {
                sh 'docker build -t kaddem .' // Build Docker image
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
