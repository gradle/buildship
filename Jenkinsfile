#!groovy

secrets = [
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

    tools {
        // https://github.com/eclipse-cbi/jiro/wiki/Tools-(JDK,-Maven,-Ant)#jdk
        jdk 'temurin-jdk8-latest'
        jdk 'temurin-jdk11-latest'
        jdk 'temurin-jdk17-latest'
    }
    
     stages {
         stage('Sanity check') {
            agent {
                label 'basic-ubuntu'
            }
            options {
                timeout(time: 10, unit: 'MINUTES')
            }
            steps {
                buildGradle("temurin-jdk11-latest", "4.34", "clean assemble checkstyleMain")
            }
        }

        stage('Basic Test Coverage') {
            matrix {
                axes {
                    // TODO: Change agent config when windows agents become available
                    axis {
                        name 'PLATFORM'
                        values 'basic-ubuntu', 'windows'
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

                // TODO: Remove this exclude when windows agents become available
                excludes {
                    exclude {
                        axis {
                            name 'PLATFORM'
                            values 'windows'
                        }
                    }
                }

                agent {
                    label '${PLATFORM}'
                }
                stages {
                    stage ('Basic Test matrix build') {
                        steps {
                            buildGradle("${JDK}", "${ECLIPSE_VERSION}", "clean eclipseTest")
                        }
                    }
                }
            }
        }

         stage('Full Test Coverage') {
             matrix {
                 axes {
                     // TODO: Change agent config when windows agents become available
                     axis {
                         name 'PLATFORM'
                         values 'basic-ubuntu', 'windows'
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
                    // TODO: Remove this exclude when windows agents become available
                    exclude {
                        axis {
                            name 'PLATFORM'
                            values 'windows'
                        }
                    }
                    exclude {
                        axis {
                            name 'PLATFORM'
                            // TODO: Change agent config when windows agents become available
                            values 'windows'
                        }
                        axis {
                            name 'ECILPSE_VERSION'
                            notValues '4.8', '4.34'
                        }
                    }
                }

                 agent {
                     label '${PLATFORM}'
                 }
                 stages {
                     stage ('Full Test matrix build') {
                         steps {
                             buildGradle("${JDK}", "${ECLIPSE_VERSION}", "clean build")
                         }
                     }
                 }
             }
         }
    }
}


def buildGradle(jdk, eclipseVersion, cmdline) {
    script {
        def eclipse = "$eclipseVersion".replace(".", "")
        withEnv(["JAVA_HOME=${ tool "$jdk" }",
            "PATH=${ tool "$jdk" }/bin:$PATH"]) {
            sh 'java --version'
            withVault([vaultSecrets: secrets]) {
                sh "./gradlew $cmdline -Peclipse.version=$eclipse -Pbuild.invoker=CI --info --stacktrace"
            }
        }
    }
}