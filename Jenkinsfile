node{
    env.JAVA_HOME="${tool 'JDK21'}"
    stage('test'){
        sh 'mvn -v && mvn -B -f ./backend clean test';
    }
}