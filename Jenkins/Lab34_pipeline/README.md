# Lab 34: Jenkins Pipeline for Application Deployment

## 🚀 CI/CD Pipeline Overview

The Jenkins pipeline automates the following steps:

1. **Run Unit Tests** – Uses Maven to run tests.
2. **Build Application** – Builds a JAR package using Maven.
3. **Build Docker Image** – Builds Docker image with `Dockerfile` from Git repo.
4. **Push Docker Image** – Pushes image to DockerHub with both `${BUILD_NUMBER}` and `latest` tags.
5. **Cleanup Local Images** – Deletes local Docker images to save space.
6. **Update Kubernetes Deployment YAML** – Replaces image tag with the new one.
7. **Deploy to Kubernetes Cluster** – Applies deployment using `kubectl`.

## 🔧 Jenkinsfile (Pipeline)

```groovy
pipeline {
    agent any

    environment {
        DOCKER_USERNAME = credentials('Docker-User')
        DOCKER_PASSWORD = credentials('Docker-Password')
        DOCKER_IMAGE = "${DOCKER_USERNAME}/jenkins-app-lab34:${BUILD_NUMBER}"
        K8S_API_SERVER = credentials('api-server')
        K8S_TOKEN = credentials('token')
        NAMESPACE = 'ivolve'
        DEPLOYMENT_FILE = 'deployment.yaml'
    }

    stages {
        stage('Run Unit Test') {
            steps {
                dir('Jenkins/Lab34_pipeline') {
                    sh 'mvn test'
                }
            }
        }

        stage('Build App') {
            steps {
                dir('Jenkins/Lab34_pipeline') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('Jenkins/Lab34_pipeline') {
                    sh """
                        docker build -t ${DOCKER_IMAGE} .
                        docker tag ${DOCKER_IMAGE} ${DOCKER_USERNAME}/jenkins-app-lab34:latest
                    """
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh """
                    echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                    docker push ${DOCKER_IMAGE}
                    docker push ${DOCKER_USERNAME}/jenkins-app-lab34:latest
                    docker logout
                """
            }
        }

        stage('Update deployment.yaml') {
            steps {
                dir('Jenkins/Lab34_pipeline/k8s') {
                    sh """
                        sed -i 's|image:.*|image: ${DOCKER_IMAGE}|' ${DEPLOYMENT_FILE}
                        cat ${DEPLOYMENT_FILE}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([
                    string(credentialsId: 'token', variable: 'K8S_TOKEN'),
                    string(credentialsId: 'api-server', variable: 'K8S_API_SERVER')
                ]) {
                    dir('Jenkins/Lab34_pipeline/k8s') {
                        sh '''
                            kubectl --server=$K8S_API_SERVER \
                                    --token=$K8S_TOKEN \
                                    --insecure-skip-tls-verify \
                                    apply -f ${DEPLOYMENT_FILE} -n ${NAMESPACE}
                        '''
                    }
                }
            }
        }

        stage('Cleanup Local Images') {
            steps {
                sh """
                    docker rmi ${DOCKER_IMAGE} || true
                    docker rmi ${DOCKER_USERNAME}/jenkins-app-lab34:latest || true
                """
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}
```

<img width="744" height="400" alt="image" src="https://github.com/user-attachments/assets/33a98cf3-ee51-4782-8463-7174425f7dae" />



<img width="956" height="436" alt="34 2" src="https://github.com/user-attachments/assets/14117292-4628-4494-8f41-1dde629b0bd5" />


