def call() {
    stage('Push Docker Image') {
        echo 'Pushing Docker image to Docker Hub...'
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub-creds', 
            usernameVariable: 'DOCKER_USER', 
            passwordVariable: 'DOCKER_PASS'
        )]) {
            script {
                try {
                    sh """
                        echo "Logging into Docker Hub as \$DOCKER_USER..."
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin

                        echo "Tagging image abeerseada/flask-app-1:\$BUILD_NUMBER as \$DOCKER_USER/flask-app-1:\$BUILD_NUMBER"
                        docker tag abeerseada/flask-app-1:\$BUILD_NUMBER \$DOCKER_USER/flask-app-1:\$BUILD_NUMBER

                        echo "Pushing image \$DOCKER_USER/flask-app-1:\$BUILD_NUMBER to Docker Hub"
                        docker push \$DOCKER_USER/flask-app-1:\$BUILD_NUMBER
                    """
                } catch (Exception e) {
                    echo "Failed to push image: ${e.message}"
                    currentBuild.result = 'FAILURE'
                    throw e
                }
                finally {
                    sh 'docker logout || true'
                    sh 'rm -f /var/jenkins_home/.docker/config.json || true'
                }
            }
        }
    }
}
return this
