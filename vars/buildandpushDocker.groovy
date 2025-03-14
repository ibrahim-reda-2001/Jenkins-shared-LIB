#!/usr/bin/env groovy
def call(String dockerCredentialsID, String repoName, String imageName, String dockerfile = 'Dockerfile') {
    def fullImageName = "${repoName}/${imageName}:${env.BUILD_NUMBER}"
    def dockerImage = docker.build(fullImageName, "-f ${dockerfile} .")
    
    withCredentials([usernamePassword(credentialsId: dockerCredentialsID, 
        usernameVariable: 'DOCKER_USERNAME', 
        passwordVariable: 'DOCKER_PASSWORD')]) {
        sh """
            echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin
            docker push ${fullImageName}
            docker rmi ${fullImageName}
        """
    }
}