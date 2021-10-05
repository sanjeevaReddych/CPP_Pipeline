@Library('shared-library') _

pipeline {
  agent any

  parameters {
    string defaultValue: 'master', name: 'Branch', trim: true
  }

  environment {
    Build_by = "Bazel"
  }

  stages {
    stage('Build') {
      steps {

        echo 'git checkout from ' + params.Branch
        git branch: 'main', credentialsId: 'd9feb0df-0fb1-4589-ab8e-aaecb3295d6e', url: 'https://github.com/bazelbuild/examples.git'
        sh 'ls -ltr'
        sh 'pwd'
        script {
          if (env.Build_by == 'Bazel') {
            sh 'pwd'
            enironment('bazel')
            echo "Building project using Bazel "
            sh 'pwd'

            sh '''
            ls -ltr cpp-tutorial
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
          ./hello-world '''
      }
    }

  }
  post {
    always {
      deleteDir()
    }
  }

}