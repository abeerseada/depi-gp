def call() {
    stage('Push Image to ECR (amd64 only)') {
    withCredentials([usernamePassword(credentialsId: 'aws-creds',
                                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                                        passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
        sh '''
        set -e
        export AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID}
        export AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY}
        export AWS_DEFAULT_REGION=${AWS_REGION}

        ECR_REG=${ECR_REGISTRY}
        ECR_REPO=${ECR_REPO}
        TAG=${BUILD_NUMBER}
        FULL_ECR=${ECR_REG}/${ECR_REPO}:${TAG}

        echo "Ensure ECR repo exists..."
        aws ecr describe-repositories --repository-names ${ECR_REPO} >/dev/null 2>&1 || \
        aws ecr create-repository --repository-name ${ECR_REPO} >/dev/null || true

        echo "Login to ECR ${ECR_REG}..."
        aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REG}

        echo "Build (amd64) and push to ECR: ${FULL_ECR}"
        docker buildx create --use 2>/dev/null || true
        docker buildx build --platform linux/amd64 \
            -t ${FULL_ECR} \
            --push \
            ./app/

        echo "Image pushed: ${FULL_ECR}"
    '''
    }
    }
    return this
}
