pipeline {
    agent any

    triggers {
        // Configure le déclenchement basé sur des push Git
        pollSCM('H/5 * * * *') // Vérifie les changements toutes les 5 minutes
    }

    stages {
        stage('Récupérer le code source') {
            steps {
                // Récupération du code depuis le référentiel Git
                git url: 'https://github.com/chedlikh/kaddem.git', branch: 'main'
            }
        }

        stage('Afficher la date système') {
            steps {
                // Affiche la date système
                script {
                    def currentDate = new Date()
                    echo "Date système : ${currentDate}"
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

