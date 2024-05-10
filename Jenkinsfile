pipeline{
    agent any
    environment {
        JAVA_HOME = tool name: 'JDK21'
        mvn = '/var/lib/maven/bin/mvn'
    }
    stages{
        stage('build'){
            steps{
                sh 'systemctl status docker'
                sh 'cd backend && mvn -B clean test';
            }
        }
    }
}