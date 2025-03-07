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

	stages {
		stage('Sanity check') {
			agent {
				label 'basic-ubuntu'
			}
			options {
				timeout(time: 1, unit: 'HOURS')
			}
			tools {
				// https://github.com/eclipse-cbi/jiro/wiki/Tools-(JDK,-Maven,-Ant)#jdk
				jdk 'temurin-jdk11-latest'
			}
			steps {
				buildGradle("Sanity check", "4.34", "clean assemble checkstyleMain")
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
						name 'JDK_VERSION'
						values 'temurin-jdk11-latest', 'temurin-jdk17-latest'
					}
					axis {
						name 'ECLIPSE_VERSION'
						values '4.8', '4.34'
					}
				}

				excludes {
					// TODO: Remove this exclude when windows agents become available
					exclude {
						axis { name 'PLATFORM'; values 'windows' }
					}
					// Limit eclipse 4.8 to jdk11
					exclude {
						axis { name 'ECLIPSE_VERSION'; values '4.8' }
						axis { name 'JDK_VERSION'; notValues 'temurin-jdk11-latest' }
                    }
					// Limit eclipse 4.34 to jdk17
					exclude {
						axis { name 'ECLIPSE_VERSION'; values '4.34' }
						axis { name 'JDK_VERSION'; notValues 'temurin-jdk17-latest' }
					}
				}

				agent {
					label "${PLATFORM}"
				}
				tools {
					jdk "${JDK_VERSION}"
				}
				stages {
					stage ('Basic Test matrix build') {
						options {
							timeout(time: 1, unit: 'HOURS')
						}
						steps {
							buildGradle("Basic Test matrix build on ${JDK_VERSION} (${PLATFORM})", "${ECLIPSE_VERSION}", "clean eclipseTest")
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
						name 'JDK_VERSION'
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
						axis { name 'PLATFORM'; values 'windows' }
					}
					// Limit windows to eclipse 4.8 and 4.34
					exclude {
						axis { name 'PLATFORM'; values 'windows' }
						axis { name 'ECLIPSE_VERSION'; notValues '4.8', '4.34' }
					}
					// Limit jdk17 to eclipse 4.25+
					exclude {
						axis { name 'JDK_VERSION'; values 'temurin-jdk17-latest' }
						axis { name 'ECLIPSE_VERSION'; values '4.8', '4.9', '4.10', '4.11', '4.12', '4.13', '4.14', '4.15', '4.16', '4.17', '4.18', '4.19', '4.20', '4.21', '4.22', '4.23', '4.24' }
					}
					// Limit eclipse 4.25+ to jdk17 (technically jdk11 to max eclipse 4.24)
					exclude {
						axis { name 'JDK_VERSION'; values 'temurin-jdk11-latest' }
						axis { name 'ECLIPSE_VERSION'; notValues '4.8', '4.9', '4.10', '4.11', '4.12', '4.13', '4.14', '4.15', '4.16', '4.17', '4.18', '4.19', '4.20', '4.21', '4.22', '4.23', '4.24' }
					}
				}

				agent {
					label "${PLATFORM}"
				}
				tools {
					jdk "${JDK_VERSION}"
				}
				stages {
					stage ('Full Test matrix build') {
						options {
							timeout(time: 1, unit: 'HOURS')
						}
						steps {
							buildGradle("Full Test matrix build on ${JDK_VERSION} (${PLATFORM})", "${ECLIPSE_VERSION}", "clean build")
						}
					}
				}
			}
		}

		stage('Cross-Version Test Coverage') {
			matrix {
				axes {
					axis {
						name 'JDK_VERSION'
						values 'temurin-jdk8-latest', 'openjdk-jdk11-latest', 'openjdk-jdk12-latest', 'openjdk-jdk13-latest', 'openjdk-jdk14-latest', 'openjdk-jdk15-latest', 'openjdk-jdk16-latest', 'openjdk-jdk17-latest'
					}
					axis {
						name 'ECLIPSE_VERSION'
						values '4.8', '4.23', '4.34'
					}
				}

				excludes {
					// Limit jdk8 to eclipse 4.8
					exclude {
						axis { name 'ECLIPSE_VERSION'; values '4.8' }
						axis { name 'JDK_VERSION'; notValues 'temurin-jdk8-latest' }
					}
					// Exclude jdk8 from eclipse 4.23 tests
					exclude {
						axis { name 'ECLIPSE_VERSION'; values '4.23' }
						axis { name 'JDK_VERSION'; values 'temurin-jdk8-latest'}
					}
					// Limit eclipse 4.34 to jdk17
					exclude {
						axis { name 'ECLIPSE_VERSION'; values '4.34' }
						axis { name 'JDK_VERSION'; notValues 'temurin-jdk17-latest' }
					}
				}

				agent {
					label "basic-ubuntu"
				}
				tools {
					jdk "${JDK_VERSION}"
				}
				stages {
					stage ('Cross-Version Test matrix build') {
						options {
							timeout(time: 1, unit: 'HOURS')
						}
						steps {
							buildGradle("Cross-Version Test matrix build on ${JDK_VERSION}", "${ECLIPSE_VERSION}", "clean eclipseTest")
						}
					}
				}
			}
		}
	}
}

def buildGradle(name, eclipseVersion, cmdline) {
	script {
		echo "Running $name with cmd '$cmdline' on Eclipse $eclipseVersion"
		def eclipse = "$eclipseVersion".replace(".", "")
		withVault([vaultSecrets: secrets]) {
			sh "./gradlew $cmdline -Peclipse.version=$eclipse -Pbuild.invoker=CI --info --stacktrace"
		}
	}
}