#!/usr/bin/env groovy
def call(String dockerCredentialsID, String repoName, String imageName, String dockerfile = 'Dockerfile' ) {
    

    // Define the full image name with tag (e.g., repoName/imageName:BUILD_NUMBER)
    def fullImageName = "${repoName}/${imageName}:${BUILD_NUMBER}"

    // Build the Docker image
    def dockerImage = docker.build(fullImageName, "-f ${dockerfile} .")

    // Push the image to the registry using credentials
    withCredentials([usernamePassword(credentialsId: dockerCredentialsID, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh """
            echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
            docker push ${fullImageName}
            docker rmi ${fullImageName}
        """
    }
}