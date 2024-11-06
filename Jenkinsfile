pipeline {
    agent any
    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }
    environment {
        SONARQUBE_ENV = 'sonarqube'
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
           steps {
               script {
                   withSonarQubeEnv("${SONARQUBE_ENV}") {
                       sh """
                           mvn sonar:sonar \
                           -Dsonar.login=${SONAR_TOKEN} \
                           -Dsonar.inclusions=src/main/java/tn/esprit/spring/services/** \
                           -Dsonar.test.inclusions=src/test/java/tn/esprit/spring/services/** \
                           -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                       """
                   }
               }
           }
       }




 }

}
