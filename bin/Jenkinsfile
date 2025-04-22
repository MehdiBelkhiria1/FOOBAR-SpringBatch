pipeline {
    agent any

    environment {
        // Customize as needed
        REGISTRY_CREDENTIALS = 'docker-credentials-id'
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_IMAGE_NAME = 'yourorg/yourapp'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/yourorg/your-repo.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile in the repo
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ."
                }
            }
        }

        stage('Push Docker Image') {
            when {
                expression {
                    // Example: only push on main branch
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    // Log in to the registry
                    withCredentials([usernamePassword(credentialsId: "${REGISTRY_CREDENTIALS}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASS} ${DOCKER_REGISTRY}"
                    }
                    // Push the image
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"
                }
            }
        }
    }

    post {
        always {
            // Clean up workspace
            cleanWs()
        }
    }
}
