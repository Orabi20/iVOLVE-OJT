// def call(String apiServer, String token, String namespace, String imageName, String deploymentFilePath) {
//     // Extract file name and parent dir
//     def file = new File(deploymentFilePath)
//     def fileName = file.getName()
//     def dirPath = file.getParent()

//     if (dirPath) {
//         // file is inside a subdirectory
//         dir(dirPath) {
//             runDeployCommands(fileName, imageName, apiServer, token, namespace)
//         }
//     } else {
//         // file is in root workspace, don't use dir()
//         runDeployCommands(fileName, imageName, apiServer, token, namespace)
//     }
// }

// def runDeployCommands(fileName, imageName, apiServer, token, namespace) {
//     // sh """
//     //     sed -i 's|image:.*|image: ${imageName}|' ${fileName}
//     //     cat ${fileName}
//     // """
//     sh """
//         kubectl --server=${apiServer} \
//                 --token=${token} \
//                 --insecure-skip-tls-verify \
//                 apply -f ${fileName} -n ${namespace} --validate=false
//     """
// }


def call(String apiServer, String token, String namespace, String image, String yamlPath) {
    echo "🔁 Updating image in ${yamlPath} to: ${image}"

    // 1. Replace the image tag in deployment.yaml
    sh """
        sed -i 's|image:.*|image: ${image}|' ${yamlPath}
        cat ${yamlPath}
    """

    // 2. Set Git identity
    sh '''
        git config --global user.email "jenkins@example.com"
        git config --global user.name "Jenkins CI"
    '''

    // 3. Commit and push the updated YAML to GitHub
    sh """
        git add ${yamlPath}
        git commit -m "🔄 Update deployment image to ${image}"
        git push origin HEAD:main
    """

    echo "✅ deployment.yaml updated and pushed to GitHub — ArgoCD will now sync it."
}
