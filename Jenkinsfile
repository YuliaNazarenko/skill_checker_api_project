pipeline {
    agent none // отключаем агент для всей декларации

    stages {
        stage('Checkout') {
            agent { label 'aws' } // checkout идёт только на Linux
            steps {
                git branch: 'main', url: 'https://github.com/YuliaNazarenko/skill_checker_api_project.git'
            }
        }

        stage('Prepare Environment') {
            agent { label 'aws' } // тоже на Linux
            steps {
                sh 'mvn clean'
            }
        }

        stage('Run Tests') {
            agent { label 'aws' }
            steps {
                sh 'mvn test'
            }
        }

        stage('Publish Results') {
            agent { label 'aws' }
            steps {
                junit 'target/surefire-reports/*.xml'
                allure([results: [[path: 'target/allure-results']]])
            }
        }
    }

    post {
        always {
            agent { label 'aws' }
            steps {
                cleanWs()
            }
        }
    }
}
