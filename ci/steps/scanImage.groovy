def call() {
    stage('Scan Docker Image') {
        echo 'Starting Docker image scan using Trivy...'
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub-creds', 
            usernameVariable: 'DOCKER_USER', 
            passwordVariable: 'DOCKER_PASS'
        )]) {
            script {
                try {
                    sh """
                        echo "Current directory: \$(pwd)"
                        echo "Logging into Docker Hub as \$DOCKER_USER"
                        echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin || true

                        echo "Running Trivy (via docker) on abeerseada/flask-app-1:\$BUILD_NUMBER..."
                        # Use dockerized Trivy, mount docker socket and a cache directory
                        docker run --rm \\
                            -v /var/run/docker.sock:/var/run/docker.sock \\
                            -v \$HOME/.cache/trivy:/root/.cache \\
                            aquasec/trivy:latest image \\
                            --exit-code 1 \\
                            --severity HIGH,CRITICAL \\
                            abeerseada/flask-app-1:\$BUILD_NUMBER
                    """
                } catch (Exception e) {
                    echo "Trivy scan reported HIGH/CRITICAL findings or failed to run: ${e.message}"
                    // Mark as unstable so the image build & push can be reviewed but not fully blocked
                    currentBuild.result = 'UNSTABLE'
                } finally {
                    // Best-effort logout (keeps workspace clean); ignore errors
                    sh 'docker logout || true'
                    sh 'rm -f /var/jenkins_home/.docker/config.json || true'
                }
            }
        }
    }
}
return this
