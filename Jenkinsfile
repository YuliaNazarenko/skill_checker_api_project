pipeline {
    agent { label 'aws' }  // весь пайплайн идёт на Linux-агенте

    options {
        skipDefaultCheckout() // отключаем автоматический checkout на master
    }

    tools {
        git 'git-linux'      // Git для Linux-агента
        maven 'Default'      // Maven
        jdk 'Default'        // JDK
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                // Явный checkout на Linux-агенте
                git branch: 'main', url: 'https://github.com/YuliaNazarenko/skill_checker_api_project.git'
            }
        }

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
