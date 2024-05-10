pipeline{
    agent any
    env.JAVA_HOME="${tool 'JDK21'}"
    stages{
        stage('test'){
            steps{
                sh 'mvn -v && mvn -B -f ./backend clean test';
            }
        }
    }
}