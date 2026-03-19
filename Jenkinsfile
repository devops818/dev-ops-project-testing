#!/usr/bin/env groovy

@Library('jenkins-shared-library')
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
    stage("build jar") {
      steps {
        script {
          buildJar()
        }
      }
    }
    stage("build image") {
      steps {
        script {
          buildImage 'janetdevop/demo-app:jma-3.1'
          dockerLogin()
          dockerPush 'janetdevop/demo-app:jma-3.1'
        }
      }
    }
    stage("test") {
      steps {
        script {
          gv.testApp()
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
