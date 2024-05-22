pipeline{
    agent {label 'built-in'}
    tools{
        maven 'maven'
        jdk 'jdk'
    }

      environment {
			PROPERTIES_LOCATION = 'backend/src/main/resources/properties/'
        }

    stages{
        stage('build'){
            steps{
                sh 'cp ${JENKINS_HOME}/mail.properties ${PROPERTIES_LOCATION}'
				sh 'cp ${JENKINS_HOME}/key.properties ${PROPERTIES_LOCATION}'
                //sh 'cd backend && mvn -B -Ptest clean verify'
                sh 'cd backend && mvn -B -Ptest -DskipTests clean verify'
            }
        }
    }
    post {
        always {
            cleanWs( cleanWhenNotBuilt: false,
                     deleteDirs: true)
        }
    }
}