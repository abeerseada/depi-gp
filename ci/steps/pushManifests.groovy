def call() {
    stage('Push Manifests') {
    withCredentials([usernamePassword(credentialsId: 'github-creds',
                                    usernameVariable: 'GIT_USER',
                                    passwordVariable: 'GIT_TOKEN')]) {  
    sh '''
        set -e

        git config user.email "jenkins@example.com"
        git config user.name "Jenkins"
        git remote set-url origin https://${GIT_USER}:${GIT_TOKEN}@github.com/abeerseada/depi-gp.git
        git add k8s/deployment.yaml || true
        git commit -m "ci: update image to ${BUILD_NUMBER}" || true
        git push origin HEAD:main || true
        '''
    }
}
return this
}
