pipeline{
    agent {label 'built-in'}
    tools{
        maven 'maven'
        jdk 'jdk'
    }
    stages{
        stage('build'){
            steps{
                sh 'groups'
                sh 'docker ps -a'
                sh 'cd backend && mvn -B clean test'
            }
        }
    }
}