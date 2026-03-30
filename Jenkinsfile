def gv

pipeline {
  agent any
  tools {
    maven 'maven-3.8.7'
  }
  parameters {
    choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
    booleanParam(name: 'executeTests', defaultValue: true, description: '')
  }
  stages {
    stage("init") {
      steps {
        script {
          gv = load "script.groovy"
        }
      }
    }
    stage("test") {
      when {
        expression {
          params.executeTests
        }
      }
      steps {
        script {
          gv.testApp()
        }
      }
    }
    stage("increment version") {
      steps {
        script {
          gv.incrementVersion()
        }
      }
    }
    stage("commit versoin update") {
      steps {
        script {
          gv.commitVersionUpdate()
        }
      }
    }
    stage("build package") {
      steps {
        script {
          gv.buildJar()
        }
      }
    }
    stage("build Image") {
      steps {
        script {
          gv.buildImage()
        }
      }
    }

    stage("deploy") {
      steps {
        script {
          // env.ENV = input message: "Select the environment to deploy to", ok: "Done", parameters: [choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: '')]
          gv.deployApp()
          // echo "Deploying to ${ENV}"
        }
      }
    }  
  }
}
