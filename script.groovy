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

def buildImage() {
  echo "building the docker image"
  withCredentials([usernamePassword(credentialsId: 'DockerHub', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'docker build -t janetdevop/demo-app:$IMAGE_NAME .'
    sh "echo $PASS | docker login -u $USER --password-stdin"
    sh 'docker push janetdevop/demo-app:$IMAGE_NAME'
  }
}

def testApp() {
  echo 'testing the application...'
}

def deployApp() {
  echo 'deploying the application...'
}

def commitVersionUpdate() {
  echo "commiting version update on git"
  withCredentials([usernamePassword(credentialsId: 'github-repo', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
    sh 'git config --global user.email "poormeintheworld@gmail.com"'
    sh 'git config --global user.name "devops818"'
    sh 'git status'
    sh 'git branch'
    sh 'git config --list'
    sh "git remote set-url origin https://${USER}:${PASS}@github.com/devops818/dev-ops-project-testing.git"
    sh 'git add .'
    sh 'git commit -m "ci: version bump"'
    sh 'git push origin HEAD:jenkins-jobs'
  }
}
return this
