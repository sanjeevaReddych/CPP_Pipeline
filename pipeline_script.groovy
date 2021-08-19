pipeline {
    agent any

    stages {
        stage("Env_setup") {
            steps {
                echo "building using bazel"
                sh '''
                sudo apt install npm
                sudo npm install -g @bazel/bazelisk
                bazel --version
                '''
            }
        }
        stage('Build') {
            steps {
                echo 'git checkout'
                git branch: 'main', credentialsId: 'git-creds', url: 'https://github.com/bazelbuild/examples'
                
                echo "Building project using Bazel"
                sh '''
                cd cpp-tutorial/stage1
                bazel build //main:hello-world
                '''
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

