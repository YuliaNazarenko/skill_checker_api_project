pipeline {
    agent { label 'aws' }

    tools {
        // используем JDK и Maven, настроенные в Jenkins Global Tool Configuration
          maven 'maven1'
    }

    stages {
        stage('Checkout') {
            steps {
                // Явно указываем, что используем Git tool с именем "Default"
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/main']],      // нужная ветка
                    userRemoteConfigs: [[url: 'https://github.com/YuliaNazarenko/skill_checker_api_project.git']],
                    gitTool: 'Default'
                ])
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
