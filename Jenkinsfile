pipeline{
    agent any
    stages{
        stage('test'){
            steps{
                echo 'zmiana'
                sh 'mvn -B -f /backend clean test';
            }
        }
    }
}