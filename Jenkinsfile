pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }
    environment {
        DOCKER_IMAGE = 'trabelsimedali-grp6-kaddem'
'  
        IMAGE_TAG = '1.1'  
    }
    stages {
        stage('Checkout') {
            steps {
                git(
                    url: 'https://github.com/chedlikh/kaddem.git', 
                    branch: 'trabelsimedali-5SAE6-grp6',
                    
                )
            }
        }

        stage('Clean, Build & Test') {
            steps {
                sh '''
                    mvn clean install
                    mvn jacoco:report
                '''
            }
        }
}
}