pipeline{
    agent any
    stages{
        stage('test'){
            steps{
                sh 'mvn -v && mvn -B -f ./backend clean test';
            }
        }
    }
}