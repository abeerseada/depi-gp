def call() {
    stage('Build Image (local, for scan)') {
    echo "Building local image for scan: ${env.BUILD_NUMBER}"
    sh '''
        set -e
        IMAGE_LOCAL=abeerseada/flask-app-1:${BUILD_NUMBER}
        echo "Ensure buildx builder exists"
        docker buildx create --use 2>/dev/null || true

        # Build amd64 image and load to local docker (use for trivy scan)
        docker buildx build --platform linux/amd64 --load -t ${IMAGE_LOCAL} ./app/
        echo "Built local image: ${IMAGE_LOCAL}"
    '''
    }
    return this
}
