pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo "Hello World"
                bat '''
                    echo Multiline Windows batch steps work too
                    dir
                '''
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
    }
}