listView('Cluster') {
	
	String[] jobNames = [ "container-mysql-client-deploy", "ecs-cluster-rds-delete", "ecs-cluster-rds-deploy" ];


	jobs {
		names(jobNames)
	}
	
	columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }

	pipelineJob(jobNames[0]) {
		displayName(jobNames[0])
		description(jobNames[0])
		parameters {
			stringParam('ACCESS_KEY_ID', '', 'Access key ID')
			stringParam('SECRET_ACCESS_KEY', '', 'Secret Access key')
			choiceParam('REGION', ["ap-south-1", "eu-west-3", "eu-north-1", "eu-west-2", "eu-west-1", "ap-northeast-2", "ap-northeast-1", "sa-east-1", "ca-central-1", "ap-southeast-1", "ap-southeast-2", "eu-central-1", "us-east-1", "us-east-2", "us-west-1", "us-west-2"], 'Region')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/ecs-mysql')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/deploy/Jenkinsfile')
				lightweight = true
			}
		}
	}

	pipelineJob(jobNames[1]) {
		displayName(jobNames[1])
		description(jobNames[1])
		parameters {
			stringParam('CLUSTER', 'us-west-1 : dev23', '<region : cluster> value pair')
			choiceParam('REGION', ["ap-south-1", "eu-west-3", "eu-north-1", "eu-west-2", "eu-west-1", "ap-northeast-2", "ap-northeast-1", "sa-east-1", "ca-central-1", "ap-southeast-1", "ap-southeast-2", "eu-central-1", "us-east-1", "us-east-2", "us-west-1", "us-west-2"], 'Region')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/cluster')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/cluster/ecs/rds/delete/Jenkinsfile')
				lightweight = true
			}
		}
	}

	pipelineJob(jobNames[2]) {
		displayName(jobNames[2])
		description(jobNames[2])
		parameters {
			stringParam('CLUSTER_NAME', 'pandora', 'Cluster Name')
			choiceParam('REGION', ["ap-south-1", "eu-west-3", "eu-north-1", "eu-west-2", "eu-west-1", "ap-northeast-2", "ap-northeast-1", "sa-east-1", "ca-central-1", "ap-southeast-1", "ap-southeast-2", "eu-central-1", "us-east-1", "us-east-2", "us-west-1", "us-west-2"], 'Region')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/cluster')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/cluster/ecs/rds/deploy/Jenkinsfile')
				lightweight = true
			}
		}
	}
}

