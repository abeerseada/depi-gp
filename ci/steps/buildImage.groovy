def call() {
    stage('Build Image ') {
        echo ' Building Docker image...'
        sh """docker build -t abeerseada/flask-app-1:$BUILD_NUMBER ."""
		echo ' Docker image built successfully.'
		currentBuild.displayName = "#${BUILD_NUMBER} - myimg"
		currentBuild.description = "Built Docker image for myimg"
    }
}
return this
