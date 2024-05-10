pipeline{
    agent any
    stages{
        stage('test'){
            steps{
                sh 'mvn -B -f /backend clean test';
            }
        }
    }
}