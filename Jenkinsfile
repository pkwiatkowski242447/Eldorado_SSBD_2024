pipeline{
    agent any
    tools{
        maven 'maven'
        jdk 'jdk'
    }

    stages{
        stage('build'){
            steps{
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}