pipeline{
    agent any
    stages{
        stage('test'){
            steps{
                env.JAVA_HOME="${tool 'JDK21'}"
                sh 'mvn -v && mvn -B -f ./backend clean test';
            }
        }
    }
}