pipeline {
    agent { label 'aws' }  // гарантируем, что всё идёт через AWS-агент

    triggers {
        githubPush()
    }

    tools {
        maven 'Default'   // настрой Maven в Jenkins (Manage Jenkins → Tools → Maven)
        jdk 'Default'     // настрой JDK аналогично
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
