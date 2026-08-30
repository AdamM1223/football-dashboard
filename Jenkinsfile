pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID     = credentials('aws-account-id')
        AWS_CREDENTIALS_ID = 'aws-ecr-credentials'
        AWS_REGION         = 'eu-central-1'

        ECR_REGISTRY       = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        BACKEND_REPO       = 'football-dashboard-backend'
        FRONTEND_REPO      = 'football-dashboard-frontend'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    SHORT_COMMIT  = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    env.IMAGE_TAG = "${BUILD_NUMBER}-${SHORT_COMMIT}"

                    dir('backend') {
                        sh "docker build -t ${BACKEND_REPO}:${env.IMAGE_TAG} ."
                    }
                    dir('frontend') {
                        sh "docker build -t ${FRONTEND_REPO}:${env.IMAGE_TAG} ."
                    }
                }
            }
        }

        stage('Authenticate to AWS ECR') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${AWS_CREDENTIALS_ID}", usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        sh '''
                            aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}
                        '''
                    }
                }
            }
        }

        stage('Tag and Push to ECR') {
            steps {
                script {
                    // Push Backend
                    sh "docker tag ${BACKEND_REPO}:${env.IMAGE_TAG} ${ECR_REGISTRY}/${BACKEND_REPO}:${env.IMAGE_TAG}"
                    sh "docker tag ${BACKEND_REPO}:${env.IMAGE_TAG} ${ECR_REGISTRY}/${BACKEND_REPO}:latest"
                    sh "docker push ${ECR_REGISTRY}/${BACKEND_REPO}:${env.IMAGE_TAG}"
                    sh "docker push ${ECR_REGISTRY}/${BACKEND_REPO}:latest"

                    // Push Frontend
                    sh "docker tag ${FRONTEND_REPO}:${env.IMAGE_TAG} ${ECR_REGISTRY}/${FRONTEND_REPO}:${env.IMAGE_TAG}"
                    sh "docker tag ${FRONTEND_REPO}:${env.IMAGE_TAG} ${ECR_REGISTRY}/${FRONTEND_REPO}:latest"
                    sh "docker push ${ECR_REGISTRY}/${FRONTEND_REPO}:${env.IMAGE_TAG}"
                    sh "docker push ${ECR_REGISTRY}/${FRONTEND_REPO}:latest"
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution finished.'
        }
        failure {
            echo 'Pipeline build failed. Check logs above for details.'
        }
        success {
            echo 'Pipeline build passed successfully!'
        }
    }
}