pipeline {
    agent any
    
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/maven-releases/'
        CREDENTIALS_ID = 'NEXUS_CRED' // replace with your actual credential ID in Jenkins
    }

    stages {
        stage('Test Nexus Repository Access') {
            steps {
                script {
                    // Test repository access by making a HEAD request
                    withCredentials([usernamePassword(credentialsId: CREDENTIALS_ID, passwordVariable: 'NEXUS_PASS', usernameVariable: 'NEXUS_USER')]) {
                        sh """
                            echo "Testing Nexus Repository Access"
                            status_code=\$(curl -s -o /dev/null -w "%{http_code}" -u \$NEXUS_USER:\$NEXUS_PASS -I $NEXUS_URL)
                            if [ "\$status_code" -eq 200 ]; then
                                echo "Access to Nexus repository is successful."
                            elif [ "\$status_code" -eq 401 ]; then
                                echo "Unauthorized: Check Nexus credentials."
                                exit 1
                            elif [ "\$status_code" -eq 404 ]; then
                                echo "Repository not found: Check the Nexus repository URL."
                                exit 1
                            else
                                echo "Error: Received status code \$status_code"
                                exit 1
                            fi
                        """
                    }
                }
            }
        }
    }
}
