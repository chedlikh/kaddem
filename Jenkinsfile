pipeline {
    agent any
    tools {
        maven "M2_HOME"  // Specify the Maven tool version
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
        DOCKER_COMPOSE_FILE = "docker-compose.yml"  // Specify your Docker Compose file
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
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true  // Wait for the quality gate
                }
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

        stage("Docker Compose Build & Push") {
            agent { label 'slave02' }
            steps {
                script {
                    // Build and push images using Docker Compose
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} build"  // Build images
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"  // Log in to Docker Hub
                    }
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} push"  // Push images to Docker Hub
                }
            }
        }

        stage("Run Docker Compose") {
            agent { label 'slave02' }
            steps {
                script {
                    // Run Docker Compose to start the containers
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} up -d"  // Start containers in detached mode
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'  // Print message at the end of the pipeline
        }
        failure {
            echo 'Échec du pipeline.'  // Print message on failure
        }
    }
}
