def buildApp() {
  echo "building the application..."
}

def buildImage() {
  echo "building the docker image..."
}

def testApp() {
  echo 'testing the application...'
  echo "Executing pipeline for branch $BRANCH_NAME"
}

def deployApp() {
  echo 'deploying the application...'
}

return this
