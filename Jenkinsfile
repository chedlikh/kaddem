pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }
    environment {
        DOCKER_IMAGE = 'trabelsimedali-grp6-kaddem'
        IMAGE_TAG = '0.0.1'
    }
    stages {
        stage('Checkout') {
            steps {
                git(
                    url: 'https://github.com/chedlikh/kaddem.git', 
                    branch: 'trabelsimedali-5SAE6-grp6'
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
  stage('Publish JaCoCo Report') {
            steps {
                // Archive the JaCoCo report
                archiveArtifacts artifacts: 'target/site/jacoco/jacoco.xml', allowEmptyArchive: true
jacoco()
            }
        }
    stage('SonarQube Analysis') {
           environment {
                SONAR_URL = "http://192.168.33.10:9000/" // URL de Sonarqube
            }
            steps {
                withCredentials([string(credentialsId: 'sonar-credentials', variable: 'SONAR_TOKEN')]) {
                    sh '''
                         mvn sonar:sonar \
                        -Dsonar.login=${SONAR_TOKEN} \
                        -Dsonar.host.url=${SONAR_URL} \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.inclusions=/src/main/java/tn/esprit/spring/kaddem/services/DepartementServiceImpl.java \
                        -Dsonar.test.inclusions=/src/test/java/tn/esprit/spring/kaddem/services/DepartementServiceImplTest.java
                    '''
                }
            }
       }

       stage('Deploy to Nexus') {
            steps {
                echo 'Deploying to Nexus'
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh 'mvn deploy -DskipTests=true -Dnexus.username=$NEXUS_USER -Dnexus.password=$NEXUS_PASS'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def nexusUrl = "http://192.168.33.10:9001"
                    def groupId = "tn.esprit.spring"
                    def artifactId = "5SAE6-grp6-kaddem"
                    def version = "1.1"

                    sh """
                        docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} \
                        --build-arg NEXUS_URL=${nexusUrl} \
                        --build-arg GROUP_ID=${groupId} \
                        --build-arg ARTIFACT_ID=${artifactId} \
                        --build-arg VERSION=${version} .
                    """
                }
            }
        }

	 stage('Push Docker Image') {
            environment {
                DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        sh "docker tag ${DOCKER_IMAGE}:${IMAGE_TAG} $DOCKER_USERNAME/${DOCKER_IMAGE}:${IMAGE_TAG}"
                        sh "docker push $DOCKER_USERNAME/${DOCKER_IMAGE}:${IMAGE_TAG}"
                    }
                }
            }
        }

	  stage('Docker Compose Up') {
            steps {
                script {         
                    sh 'docker compose up -d'
                }
            }
        }  


 }
  post {
    success {
        script {
            slackSend(
                channel: '#jenkins-messg', 
                message: "Le build a réussi : ${env.JOB_NAME} #${env.BUILD_NUMBER} ! Image pushed: ${DOCKER_IMAGE}:${IMAGE_TAG} successfully."
            )
emailext(
            subject: "Build ${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
                <h1>The build completed successfully!</h1>
                <p>Status: ${currentBuild.currentResult}</p>
                <p>Details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
            """,
            to: "trabelsi.dali484@gmail.com",
            
        )
        }

    }
    failure {
        script {
            slackSend(
                channel: '#jenkins-messg', 
                message: "Le build a échoué : ${env.JOB_NAME} #${env.BUILD_NUMBER}."
            )
        }
    }
}

}
