#!/usr/bin/env groovy
def call(String k8sCredentialsID, String k8sApiServer, String namespace = 'default', String imageName) {
    withCredentials([string(credentialsId: k8sCredentialsID, variable: 'K8S_TOKEN')]) {
        sh """
            sed -i 's|image: .*|image: ibrahimelmsery1/${imageName}|' deploy.yml
            kubectl --token=\$K8S_TOKEN \
                    --server=${k8sApiServer} \
                    --insecure-skip-tls-verify \
                    -n ${namespace} \
                    apply -f deploy.yml
        """
    }
}
