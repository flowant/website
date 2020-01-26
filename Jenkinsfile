def UUID = UUID.randomUUID().toString()
def NETWORK_ID = 'build_network_' + UUID

pipeline {

    agent any

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

        stage('Build Information') {
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

        stage('Create Network') {
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

        stage('Build') {
            agent {
                docker {
                    image 'maven:3.6.3-jdk-11-slim'
                    // label 'my-defined-label'
                    args "--network ${NETWORK_ID} -v $HOME/.m2:/root/.m2 -v $WORKSPACE:/usr/share/website"
                }
                // dockerfile {
                //     filename 'Dockerfile'
                //     additionalBuildArgs  '-t maven_builder'
                //     dir '.'
                //     args '--network ${NETWORK_ID} -v $WORKSPACE:/usr/share/website'
                // }
            }
            steps {
                sh "hostname"
                sh "printenv"
                sh "pwd"
                sh "cd /usr/share/website"
                sh "/usr/share/website/scripts/wait-for-it.sh cassandra:9042 --timeout=0 -- mvn clean install"
                // sh "mvn validate"
            }
        }

        stage('Test') {
            steps {
                sh "hostname"
                sh "pwd"
                //sh "mvn validate"
            }
        }

        stage('Deploy - Staging') {
            steps {
                sh "pwd"
                sh 'echo "Deploy - Staging"'
            }
        }

        stage('Sanity check') {
            steps {
                sh "pwd"
                sh 'echo "Sanity check"'
            }
        }

        stage('Deploy - ImageRepository for Production') {
            steps {
                sh "pwd"
                sh 'echo "Deploy - ImageRepository for Production"'
            }
        }
    }
    post {
        always {
            echo 'This will always run'
            //archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            //junit 'build/reports/**/*.xml'

            sh "hostname"
            sh "docker container stop cassandra"
            sh "docker container rm cassandra"
            echo "remove network ${NETWORK_ID}"
            sh "docker network rm ${NETWORK_ID}"

            deleteDir() // clean up our workspace
        }
        success {
            echo 'This will run only if successful'
        }
        failure {
            echo 'This will run only if failed'
            //SMTP host should be set up
            //mail to: 'flowant@gmail.com',
            //  subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
            //  body: "Something is wrong with ${env.BUILD_URL}"
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
