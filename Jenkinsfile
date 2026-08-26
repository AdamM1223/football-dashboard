pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID     = credentials('aws-account-id') // Takes value from Jenkins secret
        AWS_CREDENTIALS_ID = 'aws-ecr-credentials'
        AWS_REGION         = 'us-east-1'

        // Dynamically constructs the ECR URI
        ECR_REGISTRY       = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        BACKEND_REPO       = 'football-dashboard-backend'
        FRONTEND_REPO      = 'football-dashboard-frontend'
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Pulling latest code from GitHub..."
                checkout scm
            }
        }

        stage('Compile & Test Application') {
            steps {
                echo "Compiling Spring Boot backend..."
                dir('backend') {
                    // Ensures Maven Wrapper packages the backend cleanly
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    // Safe evaluation of Git commit short hash
                    SHORT_COMMIT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    IMAGE_TAG    = "${BUILD_NUMBER}-${SHORT_COMMIT}"

                    echo "Building Docker images with tag: ${IMAGE_TAG}"
                    sh "docker build -t ${BACKEND_REPO}:${IMAGE_TAG} ./backend"
                    sh "docker build -t ${FRONTEND_REPO}:${IMAGE_TAG} ./frontend"
                }
            }
        }

        stage('Authenticate to AWS ECR') {
            steps {
                script {
                    echo "Authenticating to AWS ECR..."
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
                    SHORT_COMMIT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    IMAGE_TAG    = "${BUILD_NUMBER}-${SHORT_COMMIT}"

                    echo "Tagging and pushing images to AWS ECR..."

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
            script {
                echo 'Cleaning up local images to free disk space...'
                sh script: 'docker image prune -f', returnStatus: true
            }
        }
        failure {
            echo 'Pipeline build failed. Check logs above for details.'
        }
    }
}