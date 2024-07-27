pipeline {
    agent any

    tools {
        maven 'maven-3.9.6'
        jdk 'jdk-17'
        git 'git' // Use the exact name configured in Jenkins
    }

    environment {
        GIT_REPO_URL = 'https://github.com/eai-12345/employee-management-system-maven.git'
        DOCKER_IMAGE = "subhashis2022/employee-management-system-maven"
        DOCKER_TAG = "${env.BUILD_ID}"
        DOCKER_REGISTRY_CREDENTIALS_ID = 'docker-auth'
        GITHUB_CREDENTIALS_ID = 'github-auth'
    }

    parameters {
        choice(name: 'BRANCH_NAME', choices: ['master', 'development', 'feature-branch'], description: 'Choose the branch to build')
        choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: 'Choose the environment')
        booleanParam(name: 'RUN_TESTS', defaultValue: true, description: 'Check if you want to run tests')
        booleanParam(name: 'RUN_PMD_ANALYSIS', defaultValue: true, description: 'Check if you want to run PMD analysis')
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    echo "Cloning repository..."
                    git branch: "${params.BRANCH_NAME}", url: "${GIT_REPO_URL}", credentialsId: "${GITHUB_CREDENTIALS_ID}"
                    echo "Repository cloned"
                }
            }
        }
        stage('Maven Clean') {
            steps {
                echo "Starting Maven Clean"
                sh './mvnw clean'
                echo "Completed Maven Clean"
            }
        }
        stage('Maven Build') {
            steps {
                echo "Starting Maven Build"
                sh './mvnw package -DskipTests'
                echo "Completed Maven Build"
            }
        }
        stage('Maven Test') {
            when {
                expression { params.RUN_TESTS }
            }
            steps {
                echo "Starting Maven Test"
                sh './mvnw test'
                echo "Completed Maven Test"
            }
        }
        stage('Jacoco Test Report') {
            steps {
                echo "Starting Jacoco Test Report"
                sh './mvnw jacoco:report'
                echo "Completed Jacoco Test Report"
            }
            post {
                always {
                    jacoco execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker Image"
                    def imageName = "${DOCKER_IMAGE}:${DOCKER_TAG}"
                    sh 'docker buildx create --use' // Ensure buildx is used
                    sh "docker buildx build -t ${imageName} ."
                    echo "Docker Image Built"
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    echo "Pushing Docker Image"
                    def imageName = "${DOCKER_IMAGE}:${DOCKER_TAG}"
                    docker.withRegistry('', "${DOCKER_REGISTRY_CREDENTIALS_ID}") {
                        sh "docker push ${imageName}"
                    }
                    echo "Docker Image Pushed"
                }
            }
        }
    }

    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
