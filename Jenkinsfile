def UUID = UUID.randomUUID().toString()
def NETWORK_ID = 'build_network_' + UUID
def builtPath

pipeline {

    agent any

    // Environment variables used in containers for test, production modes.
    environment {
        GOOGLE_CLIENT_ID = credentials('GOOGLE_CLIENT_ID')
        GOOGLE_CLIENT_SECRET = credentials('GOOGLE_CLIENT_SECRET')
        FACEBOOK_CLIENT_ID = credentials('FACEBOOK_CLIENT_ID')
        FACEBOOK_CLIENT_SECRET = credentials('FACEBOOK_CLIENT_SECRET')
        OAUTH2_KEYSTORE_STOREPASS = credentials('OAUTH2_KEYSTORE_STOREPASS')
        OAUTH2_CLIENT_ID = credentials('OAUTH2_CLIENT_ID')
        OAUTH2_CLIENT_PASSWORD = credentials('OAUTH2_CLIENT_PASSWORD')
        SSL_KEYSTORE_STOREPASS = credentials('SSL_KEYSTORE_STOREPASS')
    }

    stages {

        stage('Build Version Information') {
            steps {
                echo "UUID: ${UUID}"
                echo "NETWORK_ID: ${NETWORK_ID}"
                echo "BUILD_ID: ${env.BUILD_ID}"
                echo "BUILD_NUMBER: ${env.BUILD_NUMBER}"
                echo "BUILD_TAG: ${env.BUILD_TAG}"
                echo "BUILD_URL: ${env.BUILD_URL}"
                echo "EXECUTOR_NUMBER: ${env.EXECUTOR_NUMBER}"
                echo "JENKINS_URL: ${env.JENKINS_URL}"
                echo "JOB_NAME: ${env.JOB_NAME}"
                echo "NODE_NAME: ${env.NODE_NAME}"
                echo "WORKSPACE: ${env.WORKSPACE}"
            }
        }

        stage('Create Docker Network') {
            steps {
                sh "hostname"
                sh "pwd"
                sh "docker network create ${NETWORK_ID}"
            }
        }

        stage('Run Cassandra') {
            steps {
                sh "hostname"
                sh "pwd"
                sh "docker run --name cassandra --network ${NETWORK_ID} -d -p 9042:9042 cassandra:latest"
            }
        }

        stage('Maven Build, UnitTest') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11-slim'
                    args "--network ${NETWORK_ID} -v $HOME/.m2:/root/.m2 -v $WORKSPACE:/usr/share/website"
                }
            }
            steps {
                sh "hostname"
                sh "printenv"
                script {
                    // Save the location for built artifacts
                    builtPath = pwd()
                }
                echo "pwd:${builtPath}"
                sh "cd /usr/share/website"
                sh "/usr/share/website/scripts/wait-for-it.sh cassandra:9042 --timeout=0 -- mvn clean install"
            }
        }

        stage('Build and Push Docker Images') {
            steps {
                script {
                    // Change workspace containing built artifacts.
                    ws(builtPath) {
                        sh("hostname")
                        sh("pwd")
                        docker.withRegistry('', 'dockerhub_credential') {
                            // Build and push docker images.
                            docker.build("flowant/authserver:${env.BUILD_ID}", "./authserver").push()
                            // Remove local docker images after pushing
                            sh("docker rmi flowant/authserver:${env.BUILD_ID}")

                            docker.build("flowant/backend:${env.BUILD_ID}", "./backend").push()
                            sh("docker rmi flowant/backend:${env.BUILD_ID}")

                            docker.build("flowant/frontend:${env.BUILD_ID}", "./frontend").push()
                            sh("docker rmi flowant/frontend:${env.BUILD_ID}")

                            docker.build("flowant/gateway:${env.BUILD_ID}", "./gateway").push()
                            sh("docker rmi flowant/gateway:${env.BUILD_ID}")

                            docker.build("flowant/registry:${env.BUILD_ID}", "./registry").push()
                            sh("docker rmi flowant/registry:${env.BUILD_ID}")
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            // junit 'build/reports/**/*.xml'

            sh "hostname"
            sh "pwd"

            echo 'Remove the docker containers and network'
            sh "docker container stop cassandra"
            sh "docker container rm cassandra"
            sh "docker network rm ${NETWORK_ID}"

            echo 'Remove the workspace'
            deleteDir()
        }

        success {
            echo 'This will run only if successful'
        }

        failure {
            echo 'This will run only if failed'
        }

        unstable {
            echo 'This will run only if the run was marked as unstable'
        }

        changed {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }
    }
}
