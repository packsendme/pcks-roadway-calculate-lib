pipeline {
    agent any

    stages {
        stage('Git Checkout Repository') {
            steps {
                git branch: 'develop',
                url: 'https://github.com/packsendme/pcks-roadway-calculate-lib.git'
            }
        }
        stage('Java Build') {
          steps {
                sh 'mvn clean install'
            }
        }
    }
}