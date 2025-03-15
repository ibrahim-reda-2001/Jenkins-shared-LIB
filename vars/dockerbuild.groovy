def call(String dockerHubCredentialsID, String repoName, String imageName) {
    withCredentials([usernamePassword(credentialsId: dockerHubCredentialsID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh "echo '${PASSWORD}' | docker login -u '${USERNAME}' --password-stdin"
        sh "docker build -t ${repoName}/${imageName}:${env.BUILD_NUMBER} ."
        sh "docker push ${repoName}/${imageName}:${env.BUILD_NUMBER}"
        sh "docker rmi ${repoName}/${imageName}:${env.BUILD_NUMBER}"
    }
}
