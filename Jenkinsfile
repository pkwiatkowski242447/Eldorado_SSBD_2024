pipeline{
    agent any
    environment {
        JAVA_HOME = tool name: 'JDK21'
        mvn = '/var/lib/maven/bin/mvn'
    }
    stages{
        stage('build'){
            steps{
                sh 'set JAVA_HOME=${JAVA_HOME}'
                sh 'cd backend && ${mvn} -B clean test';
            }
        }
    }
}