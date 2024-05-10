pipeline{
    agent any

    stages{
        stage('build'){
            steps{
                sh 'echo $SHELL'
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}