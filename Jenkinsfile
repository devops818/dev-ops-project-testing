pipeline {
  agent any
  tools {
    maven 'maven-3.8.7'
  }
  stages {
    stage("build jar") {
      steps {
        script {
          echo "building the application"
          sh 'mvn package'
        }
      }
    }
    stage("build image") {
      steps {
        script {
          echo "building the docker image"
          withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
            sh 'docker build -t janetdevop/demo-app:jma-2.0 .'
            sh "echo $PASS | docker login -u $USER --password-stdin"
            sh 'docker push janetdevop/demo-app:jma-2.0'
          }
        }
      }
    }
    stage("deploy") {
      steps {
        script {
          echo "deploying the application..."
        }
      }
    } 
  }
}
