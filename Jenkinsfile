pipeline{
    agent any
    tools{
        maven 'maven'
        jdk 'jdk'
    }
    stages{
        stage('build'){
            steps{
                sh 'docker ps -a'
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}