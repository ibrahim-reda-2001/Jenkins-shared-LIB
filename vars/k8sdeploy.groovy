#!/usr/bin/env groovy
def call(String k8sCredentialsID, String k8sApiServer, String imageName, String namespace = 'default') {
    withCredentials([string(credentialsId: k8sCredentialsID, variable: 'K8S_TOKEN')]) {
        dir('Kubernetes') {  // Switch to Kubernetes directory
            sh """
                # Update image name in deployment.yml
                sed -i 's|image: .*|image: ibrahimelmsery1/${imageName}:latest|' deployment.yml
                
                # Apply all Kubernetes manifests
                kubectl --token=\$K8S_TOKEN \
                        --server=${k8sApiServer} \
                        --insecure-skip-tls-verify \
                        -n ${namespace} \
                        apply -f .
            """
        }
    }
}
