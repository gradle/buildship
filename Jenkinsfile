// allow build to publish build scans to develocity-staging.eclipse.org
def secrets = [
  [path: 'cbi/tools.buildship/develocity.eclipse.org', secretValues: [
    [envVar: 'DEVELOCITY_ACCESS_KEY', vaultKey: 'api-token']
    ]
  ]
]

pipeline {
    agent none

    environment {
        CI = "true"
    }

     triggers {
        githubPush()
    }
    
     stages {
         stage('Sanity check') {
            // Change agent config when windows agents become available
            agent {
                label 'basic-ubuntu'
            }
            //agent any
            options {
                timeout(time: 10, unit: 'MINUTES')
            }
            tools {
                // https://github.com/eclipse-cbi/jiro/wiki/Tools-(JDK,-Maven,-Ant)#jdk
                jdk 'temurin-jdk11-latest'
            }
            steps {
                withVault([vaultSecrets: secrets]) {
                    sh './gradlew assemble checkstyleMain -Peclipse.version=434 -Pbuild.invoker=CI --info --stacktrace'
                }
                
            }
        }

        stage('Basic Test Coverage') {
            matrix {
                axes {
                    // Change agent config when windows agents become available
                    axis {
                        name 'PLATFORM'
                        values 'linux', 'windows'
                    }
                    axis {
                        name 'JDK'
                        values 'temurin-jdk11-latest', 'temurin-jdk17-latest'
                    }
                    axis {
                        name 'ECLIPSE_VERSION'
                        values '4.8', '4.34'
                    }
                }

                // Remove this exclude when windows agents become available
                excludes {
                    exclude {
                        axis {
                            name 'PLATFORM'
                            values 'windows'
                        }
                    }
                }

                // Change agent config when windows agents become available
                //agent {
                //    label '${PLATFORM}'
                //}
                agent any
                stages {
                    stage ('Basic Test matrix build') {
                        steps {
                            tool name: "${JDK}", type: 'jdk'
                            script {
                                def eclipseVersion = "${ECLIPSE_VERSION}".replace(".", "")
                                withVault([vaultSecrets: secrets]) {
                                    sh './gradlew clean eclipseTest -Peclipse.version=${ECLIPSE_VERSION} -Pbuild.invoker=CI --info --stacktrace'
                                }
                            }
                        }
                    }
                }
            }
        }

         stage('Full Test Coverage') {
             matrix {
                 axes {
                     // Change agent config when windows agents become available
                     axis {
                         name 'PLATFORM'
                         values 'linux', 'windows'
                     }
                     axis {
                         name 'JDK'
                         values 'temurin-jdk11-latest', 'temurin-jdk17-latest'
                     }
                     axis {
                         name 'ECLIPSE_VERSION'
                         values '4.8', '4.9', '4.10', '4.11', '4.12', '4.13', '4.14', '4.15', '4.16', '4.17', '4.18', '4.19', '4.20', '4.21', '4.22', '4.23', '4.24', '4.25', '4.26', '4.27', '4.28', '4.29', '4.30', '4.31', '4.32', '4.33', '4.34'
                     }
                 }

                excludes {
                    // Remove this exclude when windows agents become available
                    exclude {
                        axis {
                            name 'PLATFORM'
                            values 'windows'
                        }
                    }
                    exclude {
                        axis {
                            name 'PLATFORM'
                            values 'windows'
                        }
                        axis {
                            name 'ECILPSE_VERSION'
                            notValues '4.8', '4.34'
                        }
                    }
                }

                 // Change agent config when windows agents become available
                 //agent {
                 //    label '${PLATFORM}'
                 //}
                 agent any
                 stages {
                     stage ('Full Test matrix build') {
                         steps {
                             tool name: "${JDK}", type: 'jdk'
                             script {
                                 def eclipseVersion = "${ECLIPSE_VERSION}".replace(".", "")

                                 withVault([vaultSecrets: secrets]) {
                                 sh './gradlew clean build -Peclipse.version=${ECLIPSE_VERSION} -Pbuild.invoker=CI --info --stacktrace'
                                 }
                             }
                         }
                     }
                 }
             }
         }
    }
}