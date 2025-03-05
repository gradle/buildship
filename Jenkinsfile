// allow build to publish build scans to develocity-staging.eclipse.org
def secrets = [
  [path: 'cbi/tools.buildship/develocity.eclipse.org', secretValues: [
    [envVar: 'DEVELOCITY_ACCESS_KEY', vaultKey: 'api-token']
    ]
  ]
]

pipeline {
    agent none
    tools {
        // https://github.com/eclipse-cbi/jiro/wiki/Tools-(JDK,-Maven,-Ant)#jdk
        jdk 'temurin-jdk11-latest'
    }

    environment {
        CI = "true"
    }

     triggers {
        githubPush()
    }
    
     stages {
        stage('Sanity check') {
            agent any
            steps {
                withVault([vaultSecrets: secrets]) {
                    sh './gradlew assemble checkstyleMain -Peclipse.version=434 -Pbuild.invoker=CI --info --stacktrace'
                }
                
            }
        }

        stage('Basic Test Coverage') {
            matrix {
                axes {
                    axis {
                        name 'PLATFORM'
                        values 'linux'//, 'windows'
                    }
                    axis {
                        name 'JDK'
                        values 'temurin-jdk11-latest', 'temurin-jdk17-latest'
                    }
                    axis {
                        name 'ECLIPSE_VERSION'
                        values '48', '434'
                    }
                }

                agent {
                    label '${PLATFORM}'
                }
                tools {
                    jdk '${JDK}'
                }
                stage {
                    steps {
                        withVault([vaultSecrets: secrets]) {
                            sh './gradlew clean eclipseTest -Peclipse.version=${ECLIPSE_VERSION} -Pbuild.invoker=CI --info --stacktrace'
                        }
                    }
                }
            }
        }

    }
}