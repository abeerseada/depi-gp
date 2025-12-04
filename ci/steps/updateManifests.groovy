def call() {
    stage('Update Manifests') {
        echo 'Updating Kubernetes manifests (deployment.yaml)...'

        sh """
            set -e
            ECR_REG=${ECR_REGISTRY}
            ECR_REPO=${ECR_REPO}
            TAG=${BUILD_NUMBER}
            FULL_ECR=${ECR_REG}/${ECR_REPO}:${TAG}
            sed -i 's|image: .*|image: ${FULL_ECR}|' k8s/deployment.yaml
            echo "Manifest updated to image: ${FULL_ECR}"
        """
    }
}
return this
