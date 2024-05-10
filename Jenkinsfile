pipeline{
    agent any
    environment {
        JAVA_HOME = tool name: 'JDK21'
    }
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