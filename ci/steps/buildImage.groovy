def call() {
    stage('Build Image') {
        echo 'Building Docker image...'

        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-creds',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )
        ]) {
            sh """
                echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin

                docker buildx build \
                --platform linux/amd64 \
                -t abeerseada/flask-app-1:${env.BUILD_NUMBER} \
                --push ./app/.

                docker logout
            """
        }

        echo 'Docker image built successfully.'
        currentBuild.displayName = "#${env.BUILD_NUMBER} - myimg"
        currentBuild.description = "
