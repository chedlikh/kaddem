pipeline {
    agent any
    tools {
        maven "M2_HOME"  // Specify the Maven tool version
        terraform 'terraform'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))  // Keep the last 5 builds
    }

    environment {
        // Environment variables for Nexus
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.33.10:8081"
        NEXUS_REPOSITORY = "maven-central-repository"
        NEXUS_CREDENTIAL_ID = "NEXUS_CRED"
        ARTIFACT_VERSION = "${BUILD_NUMBER}"

        // Environment variables for Docker
        DOCKER_CREDENTIALS_ID = "docker-hub-creds"
        DOCKER_IMAGE = "chedli1/kaddem"
        DOCKER_TAG = "v1.0.0-${BUILD_NUMBER}"
        DOCKER_COMPOSE_FILE = "docker-compose.yml"  
        EMAIL_RECIPIENT = 'chdouuulaa@gmail.com'  
        EMAIL_SUBJECT = 'Jenkins Build Notification'
    }

    stages {
        stage("Check out") {
            agent { label 'slave02' }  // Run this stage on slave02
            steps {
                script {
                    git branch: 'chedli', url: 'https://github.com/chedlikh/kaddem.git'
                }
            }
        }

        stage("Maven Build") {
            agent { label 'slave02' }  // Run this stage on slave02
            steps {
                script {
                    sh "mvn clean compile package"  // Clean and build the project
                    sh "ls -la target"              // List contents of target directory
                }
            }
        }

        stage("Publish to Nexus") {
            steps {
                script {
                    def pom = readMavenPom file: "pom.xml"
                    def filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                    def artifactPath = filesByGlob[0]?.path  // Safe access with optional chaining
                    def artifactExists = fileExists(artifactPath)

                    if (artifactExists) {
                        echo "*** Publishing artifact: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version: ${pom.version}"

                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: ARTIFACT_VERSION,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging]
                            ]
                        )
                    } else {
                        error "*** File: ${artifactPath}, could not be found"
                    }
                }
            }
        }

        stage("SonarQube Analysis") {
            steps {
                withSonarQubeEnv('sq1') {
                    sh 'mvn sonar:sonar'  // Run SonarQube analysis
                }
            }
        }

        stage("Quality Gate") {
            steps {
                waitForQualityGate abortPipeline: false  // Wait for the quality gate
            }
        }

        stage("Download Artifact from Nexus") {
            agent { label 'slave02' }
            steps {
                script {
                    def artifactGroup = 'tn.esprit.spring'
                    def artifactName = 'kaddem'
                    def artifactVersion = 'v2.0'
                    def artifactExtension = 'jar'

                    def nexusArtifactURL = "${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY}/${artifactGroup.replace('.', '/')}/${artifactName}/${artifactVersion}/${artifactName}-${artifactVersion}.${artifactExtension}"

                    withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIAL_ID, passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                        sh "curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -o ${artifactName}.jar ${nexusArtifactURL}"  // Download the artifact
                    }
                }
            }
        }

        stage("DockerHUB") {
            agent { label 'slave02' }
            steps {
                script {
                    // Build Docker image using Dockerfile
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    
                    // Log in to Docker Hub
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                    }

                    // Push Docker image to Docker Hub
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage("Docker Compose Build & Push") {
            agent { label 'slave02' }
            steps {
                script {
                    // Build and push images using Docker Compose
                    sh "docker compose -f ${DOCKER_COMPOSE_FILE} build"  // Build images
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"  // Log in to Docker Hub
                    }
                    sh "docker compose -f ${DOCKER_COMPOSE_FILE} push"  // Push images to Docker Hub
                }
            }
        }

        stage("Run Docker Compose") {
            agent { label 'slave02' }
            steps {
                script {
                    // Run Docker Compose to start the containers
                    sh "docker compose -f ${DOCKER_COMPOSE_FILE} up -d"  // Start containers in detached mode
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
        success {
            emailext(
                subject: "${EMAIL_SUBJECT}: Success",
                body: """ 
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: green;">Build Status: SUCCESS</h2>
                        <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                        <p><strong>Build Name:</strong> ${currentBuild.fullDisplayName}</p>
                        <p><strong>Build URL:</strong> <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                        <p><strong>Logs:</strong> <a href="${currentBuild.absoluteUrl}console">Click here to view logs</a></p>
                        <h3>Pipeline Stages:</h3>
                        <ul>
                            <li><strong>Checkout:</strong> Git repository checkout from branch 'chedli'.</li>
                            <li><strong>Maven Build:</strong> Maven project build completed.</li>
                            <li><strong>Publish to Nexus:</strong> Artifact successfully uploaded to Nexus.</li>
                            <li><strong>SonarQube Analysis:</strong> Code quality analysis completed.</li>
                            <li><strong>Quality Gate:</strong> Passed quality gate check.</li>
                            <li><strong>DockerHUB:</strong> Docker image built and pushed to Docker Hub.</li>
                            <li><strong>Docker Compose Build & Push:</strong> Docker images built and pushed with Docker Compose.</li>
                            <li><strong>Run Docker Compose:</strong> Containers started with Docker Compose.</li>
                        </ul>
                    </body>
                </html>
                """,
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENT}"
            )
        }
        failure {
            emailext(
                subject: "${EMAIL_SUBJECT}: Failed",
                body: """ 
                <html>
                    <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: red;">Build Status: FAILURE</h2>
                        <p><strong>Build Number:</strong> ${BUILD_NUMBER}</p>
                        <p><strong>Build Name:</strong> ${currentBuild.fullDisplayName}</p>
                        <p><strong>Build URL:</strong> <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                        <p><strong>Logs:</strong> <a href="${currentBuild.absoluteUrl}console">Click here to view logs</a></p>
                    </body>
                </html>
                """,
                mimeType: 'text/html',
                to: "${EMAIL_RECIPIENT}"
            )
        }
    }
}
