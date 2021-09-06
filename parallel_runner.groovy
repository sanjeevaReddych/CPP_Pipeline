pipeline {
    agent any
    
    stages {
        stage('paralle job') {
            parallel {
                stage('main_branch'){
                    steps{
                        build job: 'cpp_test', parameters: [string(name: 'Branch', value: 'main')]
                    }
                }
                
                stage('bs_branch'){
                    steps{
                        build job: 'cpp_test', parameters: [string(name: 'Branch', value: 'bs')]
                    }
                }
            }
        }
    
        }
}


