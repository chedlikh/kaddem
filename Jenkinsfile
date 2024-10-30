pipeline {
    agent any
    stages {
        stage('Test Docker Access') {
            steps {
                script {
                    sh 'docker login'
                }
            }
        }
    }
}
