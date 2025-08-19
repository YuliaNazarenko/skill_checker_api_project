pipeline {
    agent { label 'aws' }


    stages {
        stage('Checkout') {
            steps {
                // Просто checkout, Jenkins сам выберет Git
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh '''
                    echo "Building and testing project with Maven..."
                    mvn clean test
                '''
            }
        }
    }

    post {
        success {
            echo 'Build and tests passed successfully!'
        }
        failure {
            echo 'Build or tests failed.'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}
