pipeline{
    agent any

    stages{
        stage('build'){
            steps{
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}