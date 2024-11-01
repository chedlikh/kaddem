pipeline {
     agent any
    tools {
        maven "M2_HOME"
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.33.10:8081"
        NEXUS_REPOSITORY = "maven-central-repository"
        NEXUS_CREDENTIAL_ID = "NEXUS_CRED"
        ARTIFACT_VERSION = "${BUILD_NUMBER}"
        DOCKER_CREDENTIALS_ID = "docker-hub-creds"
        DOCKER_IMAGE = "chedli1/kaddem"
        DOCKER_TAG = "v1.0.0-${BUILD_NUMBER}"
    }

    stages {
        stage("Check out") {
            steps {
                script {
                    git branch: 'chedli', url: 'https://github.com/chedlikh/kaddem.git'
                    checkout scm
                }
            }
        }

        stage("Maven Build") {
            steps {
                script {
                    sh "mvn clean compile package"
                    sh "ls -la target" // List contents of target directory
                }
            }
        }

        stage("Publish to Nexus") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml"
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path
                    artifactExists = fileExists artifactPath

                    if (artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}"

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


        stage("Download Artifact from Nexus") {
            steps {
                script {
                    def artifactGroup = 'tn.esprit.spring'
                    def artifactName = 'kaddem'
                    def artifactVersion = 'v2.0'
                    def artifactExtension = 'jar'

                    def nexusArtifactURL = "${NEXUS_PROTOCOL}://${NEXUS_URL}/repository/${NEXUS_REPOSITORY}/${artifactGroup.replace('.', '/')}/${artifactName}/${artifactVersion}/${artifactName}-${artifactVersion}.${artifactExtension}"

                    withCredentials([usernamePassword(credentialsId: "${NEXUS_CREDENTIAL_ID}", passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                        sh "curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -o ${artifactName}.jar ${nexusArtifactURL}"
                    }
                }
            }
        }

        stage("Docker Build & Push") {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                    }
                    sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage("Run Docker Container") {
            steps {
                script {
                    sh """
                        docker ps -q --filter "name=kaddemc" | grep -q . && docker stop kaddemc || true
                        docker rm kaddemc || true
                    """
                    sh "docker run -d --name kaddemc -p 8096:8096 ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé.'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}
