pipeline {
    agent any
    stages {
        stage('Check Docker') {
            steps {
                script {
                    // Print Docker version to confirm connectivity
                    try {
                        sh 'docker --version'
                        echo "Docker is available on this agent."
                    } catch (Exception e) {
                        echo "Docker is not available on this agent."
                    }
                    
                    // Run a basic Docker command to list containers
                    try {
                        sh 'docker ps'
                    } catch (Exception e) {
                        echo "Failed to execute 'docker ps'. Please check permissions or Docker installation."
                    }
                }
            }
        }
    }
}
