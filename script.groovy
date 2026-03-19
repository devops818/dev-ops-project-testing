def buildApp() {
  echo "building the application..."
}

def buildJar() {
  echo "building the application"
  sh 'mvn package'
}

def buildImage() {
  echo "building the docker image"
  withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker build -t janetdevop/demo-app:jma-2.0 .'
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh 'docker push janetdevop/demo-app:jma-2.0'
  }
}

def testApp() {
  echo 'testing the application...'
  echo "Executing pipeline for branch $BRANCH_NAME"
}

def deployApp() {
  echo 'deploying the application...'
}

return this
