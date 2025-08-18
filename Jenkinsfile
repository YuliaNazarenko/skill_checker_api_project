pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat 'echo Hello World'  //Коммент ще
                bat '''
                    echo Multiline shell steps works too
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
post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}