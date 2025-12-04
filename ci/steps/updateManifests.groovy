def call() {
    stage('Update Manifests') {
        echo 'Updating Kubernetes manifests (deployment.yaml)...'

        sh """
            sed -i 's|image: .*|image: abeerseada/flask-app-1:${BUILD_NUMBER}|' k8s/deployment.yaml
            echo "Manifest updated to image: abeerseada/flask-app-1:${BUILD_NUMBER}"
        """
    }
}
return this
