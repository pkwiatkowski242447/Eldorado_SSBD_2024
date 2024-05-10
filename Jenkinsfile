pipeline{
    agent any
    environment {
        JDK = tool name: 'JDK21'
    }
    stages{
        stage('test'){
            steps{
                sh 'set JAVA_HOME=${JDK}'
                sh 'mvn -v && mvn -B -f ./backend clean test';
            }
        }
    }
}