def call() {
    stage('Build Image') {
        echo 'Building Docker image...'

        sh """
            docker buildx build \
            --platform linux/amd64 \
            -t abeerseada/flask-app-1:${BUILD_NUMBER} \
            --push ./app/.
        """

        echo 'Docker image built successfully.'
        currentBuild.displayName = "#${BUILD_NUMBER} - myimg"
        currentBuild.description = "Built Docker image for myimg"
    }
}

return this
