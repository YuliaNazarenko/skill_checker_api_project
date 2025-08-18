pipeline {
    agent { label 'aws' }

    options { skipDefaultCheckout() }

    tools {
        git 'git-linux'
        maven 'Default'
        jdk 'Default'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // ручной checkout с использованием Linux Git
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/YuliaNazarenko/skill_checker_api_project.git']]
                    ])
                }
            }
        }

        stage('Prepare Environment') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Publish Results') {
            steps {
                junit 'target/surefire-reports/*.xml'
                allure([results: [[path: 'target/allure-results']]])
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
