pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }
    environment {
        DOCKER_IMAGE = 'trabelsimedali-grp6-kaddem'
        IMAGE_TAG = '1.1'
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
    stage('SonarQube Analysis') {
           environment {
                SONAR_URL = "http://192.168.33.10:9000/" // URL de SonarCloud test webhook test test
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


 }

}
