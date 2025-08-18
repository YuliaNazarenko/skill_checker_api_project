pipeline {
    agent { label 'aws-agent' }   // используем конкретный Linux агент
        tools {
            git 'Git-Linux'           // используем Linux Git из Global Tool Configuration
        }
    stages {

        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}