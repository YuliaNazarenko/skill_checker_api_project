pipeline {
    agent { label 'aws' }

    triggers {
        githubPush()
    }

    stages {
        stage('Prepare Environment') {
            steps {
                echo "Cleaning and preparing..."
                sh 'mvn clean'
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests..."
                sh 'mvn test'
            }
        }

        stage('Publish Results') {
            steps {
                echo "Publishing test results..."
                junit 'target/surefire-reports/*.xml'
                allure([
                    includeProperties: false,
                    jdk: '',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
    }
}