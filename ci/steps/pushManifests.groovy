def call() {
    stage('Push Manifests') {
        withCredentials([usernamePassword(
            credentialsId: 'github-creds', 
            usernameVariable: 'GIT_USER', 
            passwordVariable: 'GIT_TOKEN'
        )]) {

            sh """
                echo "Configuring git for commit..."
                git config user.email "abeerseada148@gmail.com"
                git config user.name "abeerseada"

                echo "Setting remote URL with token..."
                git remote set-url origin https://${GIT_USER}:${GIT_TOKEN}@github.com/abeerseada/depi-gp.git

                echo "Adding updated manifests..."
                git add k8s/deployment.yaml

                echo "Committing changes..."
                git commit -m "Update image tag to build ${BUILD_NUMBER}" || true

                echo "Pushing changes to GitHub..."
                git push origin HEAD:main
            """
        }
    }
}
return this

