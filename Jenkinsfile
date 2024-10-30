pipeline {
    agent any
    
    tools {
        maven 'M2_HOME' // Nom de votre installation Maven dans Jenkins
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Cloner le code source depuis votre système de contrôle de version (Git, par exemple)
                git url: 'https://github.com/chedlikh/kaddem.git', branch: 'main'
            }
        }
        stage('Check Maven Repository Access') {
    steps {
        sh 'curl -I http://192.168.33.11:8081/repository/maven-releases/'
    }
}

        
        stage('Build') {
            steps {
                script {
                    // Construire le projet et déployer sur Nexus en une seule commande
                    // Skip tests (en ajoutant -DskipTests) si besoin
                    sh 'mvn clean deploy -DskipTests'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Le déploiement sur Nexus a été effectué avec succès !'
        }
        failure {
            echo 'Échec du déploiement sur Nexus.'
        }
    }
}
