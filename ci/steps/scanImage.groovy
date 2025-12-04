def call() {
  stage('Scan Image (Trivy)') {
    echo "Scanning local image with Trivy (HIGH/CRITICAL -> mark UNSTABLE)"
    script {
      try {
        sh '''
          set -e
          IMAGE_LOCAL=abeerseada/flask-app-1:${BUILD_NUMBER}
          docker run --rm \
            -v /var/run/docker.sock:/var/run/docker.sock \
            -v $HOME/.cache/trivy:/root/.cache \
            aquasec/trivy:latest image --exit-code 1 --severity HIGH,CRITICAL ${IMAGE_LOCAL}
        '''
      } catch (Exception e) {
        echo "Trivy found HIGH/CRITICAL issues or failed: ${e.message}"
        currentBuild.result = 'UNSTABLE'
      }
    }
  }
  return this
}
