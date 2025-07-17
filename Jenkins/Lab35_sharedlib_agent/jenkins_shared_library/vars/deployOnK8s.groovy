def call(String apiServer, String token, String namespace, String imageName, String deploymentFilePath) {
    // Extract file name and parent dir
    def file = new File(deploymentFilePath)
    def fileName = file.getName()
    def dirPath = file.getParent()

    if (dirPath) {
        // file is inside a subdirectory
        dir(dirPath) {
            runDeployCommands(fileName, imageName, apiServer, token, namespace)
        }
    } else {
        // file is in root workspace, don't use dir()
        runDeployCommands(fileName, imageName, apiServer, token, namespace)
    }
}

def runDeployCommands(fileName, imageName, apiServer, token, namespace) {
    // sh """
    //     sed -i 's|image:.*|image: ${imageName}|' ${fileName}
    //     cat ${fileName}
    // """
    sh """
        kubectl --server=${apiServer} \
                --token=${token} \
                --insecure-skip-tls-verify \
                apply -f ${fileName} -n ${namespace} --validate=false
    """
}


