pipeline{
    agent {label 'built-in'}
    tools{
        maven 'maven'
        jdk 'jdk'
    }

      environment {
            MAIL_PROPERTIES_TARGET_LOCATION = 'backend/src/main/resources/properties/'
        }

    stages{
        stage('build'){
            steps{
                sh 'cp ${JENKINS_HOME}/mail.properties ${MAIL_PROPERTIES_TARGET_LOCATION}'
                sh 'cd backend && mvn -B -Ptest clean test'
            }
        }
    }
}