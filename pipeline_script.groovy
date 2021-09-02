pipeline {
    agent any

      parameters {
                string defaultValue: 'main', name: 'Branch', trim: true
                }

      environment {
                Build_by = "Bazel"
      }

    stages {
        stage('Build') {
            steps {
                echo 'git checkout from '+ params.Branch
                git branch: params.Branch, credentialsId: 'git-creds', url: 'https://github.com/bazelbuild/examples'

                script {
                    if (env.Build_by == 'Bazel'){
                        echo "Env setup for Bazel"
                        sh '''
                            sudo apt install npm
                            sudo npm install -g @bazel/bazelisk
                            bazel --version
                            '''
                        echo "Building project using Bazel"
                        sh '''
                            cd cpp-tutorial/stage1
                            bazel build //main:hello-world
                            '''
                    } else {
                    echo "This build system is not configured"
                    }
                }
            }
        }
        stage('test') {
            steps {
                sh '''
                cd cpp-tutorial/stage1/bazel-bin/main
                ./hello-world
                '''
            }
        }
    }
}

