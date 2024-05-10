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
                sh 'pwd'
                sh 'ls'
                sh 'cp mail.properties ${MAIL_PROPERTIES_TARGET_LOCATION}'
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}