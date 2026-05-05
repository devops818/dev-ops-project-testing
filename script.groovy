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
      
      # 1. Switch to the target branch first
      git checkout jenkins-jobs
      git pull origin jenkins-jobs
      
      # 2. If you stashed changes earlier, pop them now
      # git stash pop 
      
      # 3. Add and commit
      git add .
      
      # 4. Use || true to prevent the pipeline from failing if there's nothing to update
      git commit -m "[jenkins] ci: version bump" || echo "No changes to commit"
      
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

def deployAppEC2() {
  echo 'deploying the docker to EC2...'
  // def dockerCmd = 'docker run -p 8080:8080 -d janetdevop/demo-app:jma-${IMAGE_NAME}'
  // def dockerComposeCmd = "docker-compose -f /home/ec2-user/docker-compose.yaml up --detach"
  def shellCmd = "bash ./server-cmds.sh janetdevop/demo-app:jma-${IMAGE_NAME}"
  sshagent(['ec2-key']) {
    sh "scp docker-compose.yaml ec2-user@35.168.36.104:/home/ec2-user"
    sh "scp server-cmds.sh ec2-user@35.168.36.104:/home/ec2-user"
    sh "ssh -o StrictHostKeyChecking=no ec2-user@35.168.36.104 ${shellCmd}"
  }
}

def deployApp() {
  env.APP_NAME = 'java-maven-app'
  echo 'deploying the docker from dockerhub to EKS...'
  sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f -'
}

def testApp() {
  echo 'testing the application...'
}

return this
