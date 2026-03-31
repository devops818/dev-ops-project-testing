def buildApp() {
  echo "building the application..."
}

def buildJar() {
  echo "building the application"
  sh 'mvn clean package'
}

def incrementVersion() {
  echo 'incrementing app version...'
  sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
  def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
  def version = matcher[0][1]
  env.IMAGE_NAME = "$version-$BUILD_NUMBER"
}

def commitVersionUpdate() {
  echo "commiting version update on git but ignoring this commit"
  withCredentials([usernamePassword(credentialsId: 'github-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh '''
      git config --global user.email "jenkins@example.com"
      git config --global user.name "jenkins"
      git remote set-url origin https://${USER}:${PASS}@github.com/devops818/dev-ops-project-testing.git
      git checkout jenkins-jobs
      git pull origin jenkins-jobs
      git add .
      git commit -m "[jenkins] ci: version bump"
      git push origin HEAD:jenkins-jobs
    '''
  }
}

def buildImage() {
  echo "building the docker image"
  withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker build -t janetdevop/demo-app:jma-$IMAGE_NAME .'
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh 'docker push janetdevop/demo-app:jma-$IMAGE_NAME'
  }
}

def deployApp() {
  echo 'deploying the docker to EC2...'
  // def dockerCmd = 'docker run -p 3080:8080 -d janetdevop/demo-app:jma-${IMAGE_NAME}'
  // def dockerComposeCmd = "docker-compose -f /home/ec2-user/docker-compose.yaml up --detach"
  def shellCmd = "bash ./server-cmd.sh"
  sshagent(['ec2-key']) {
    sh "scp docker-compose.yaml ec2-user@98.82.113.126:/home/ec2-user"
    sh "scp server-cmd.sh ec2-user@98.82.113.126:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ec2-user@98.82.113.126 ${shellCmd}"
  }
}

def testApp() {
  echo 'testing the application...'
}

return this
