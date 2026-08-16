pipeline {
    agent any

    environment {
        // PLACEHOLDERS: Replace with actual values
        AWS_ACCOUNT_ID = '123456789012'              // Replace with AWS Account ID
        AWS_REGION     = 'us-east-1'                 // Replace with target AWS Region (e.g. eu-west-1, us-east-1)
        ECR_REGISTRY   = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

        // AWS ECR Repository Names (Must match repos created in ECR console)
        BACKEND_REPO   = 'football-dashboard-backend'
        FRONTEND_REPO  = 'football-dashboard-frontend'

        // Jenkins Credential ID for AWS access
        AWS_CREDENTIALS_ID = 'aws-ecr-credentials'

        // Generate dynamic tag based on Jenkins build number and Git commit hash
        IMAGE_TAG      = "${BUILD_NUMBER}-${GIT_COMMIT.take(7)}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Pulling latest code from GitHub..."
                checkout scm
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo "Building Frontend and Backend Docker images..."
                    sh "docker build -t ${BACKEND_REPO}:${IMAGE_TAG} ./backend"
                    sh "docker build -t ${FRONTEND_REPO}:${IMAGE_TAG} ./frontend"
                }
            }
        }

        stage('Authenticate to AWS ECR') {
            steps {
                script {
                    echo "Authenticating to AWS ECR..."
                    // Uses Jenkins Credentials Plugin to inject AWS Access Keys securely
                    withCredentials([usernamePassword(
                        credentialsId: "${AWS_CREDENTIALS_ID}",
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    )]) {
                        sh """
                            aws configure set aws_access_key_id ${AWS_ACCESS_KEY_ID}
                            aws configure set aws_secret_access_key ${AWS_SECRET_ACCESS_KEY}
                            aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                        """
                    }
                }
            }
        }

        stage('Tag and Push to ECR') {
            steps {
                script {
                    echo "Tagging and pushing images to ECR..."

                    // Backend: Push versioned tag + latest tag
                    sh "docker tag ${BACKEND_REPO}:${IMAGE_TAG} ${ECR_REGISTRY}/${BACKEND_REPO}:${IMAGE_TAG}"
                    sh "docker tag ${BACKEND_REPO}:${IMAGE_TAG} ${ECR_REGISTRY}/${BACKEND_REPO}:latest"
                    sh "docker push ${ECR_REGISTRY}/${BACKEND_REPO}:${IMAGE_TAG}"
                    sh "docker push ${ECR_REGISTRY}/${BACKEND_REPO}:latest"

                    // Frontend: Push versioned tag + latest tag
                    sh "docker tag ${FRONTEND_REPO}:${IMAGE_TAG} ${ECR_REGISTRY}/${FRONTEND_REPO}:${IMAGE_TAG}"
                    sh "docker tag ${FRONTEND_REPO}:${IMAGE_TAG} ${ECR_REGISTRY}/${FRONTEND_REPO}:latest"
                    sh "docker push ${ECR_REGISTRY}/${FRONTEND_REPO}:${IMAGE_TAG}"
                    sh "docker push ${ECR_REGISTRY}/${FRONTEND_REPO}:latest"
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up local images to free disk space..."
            sh "docker image prune -f"
        }
        success {
            echo "Successfully pushed images to AWS ECR!"
        }
        failure {
            echo "Pipeline build failed. Check logs above for details."
        }
    }
}