pipeline{
    agent any
    environment {
        JAVA_HOME = tool name: 'JDK21'
    }
    stages{
        stage('test'){
            steps{
                sh 'set JAVA_HOME=${JAVA_HOME}'
                sh 'mvn -v && mvn -B -f backend clean package';
            }
        }
    }
}