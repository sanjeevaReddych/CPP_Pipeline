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

        script {
          if (env.Build_by == 'Bazel') {
            bazel_helper.prepare_environment()

            echo "Building project using Bazel "
            bazel_helper.create_build(build_path: 'cpp-tutorial/stage1',
                                      build_function: 'hello-world')

           
          } else {
            echo "This build system is not configured"
          }
        }
      }
    }
    stage('test') {
	
      steps {
	script{
		def ret = sh(script:  ''' cd cpp-tutorial/stage1/bazel-bin/main
          ./hello-world ''', returnStdout: true)
		echo ret
		if (test_output(ret)==true){
			echo "test passes"
				}else{
					echo "test failed"
				}
		}
	
        	
      }
    }

  }
  post {
    always {
      deleteDir()
    }
  }

}
