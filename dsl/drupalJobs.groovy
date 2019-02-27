listView('Drupal') {
	
	String[] jobNames = [ "drupal-deploy", "drupal-delete", "drupal-clone-databases", "drupal-restore-from-bucket" ];


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
			stringParam('SITE_ID', '', 'Cluster Name')
			stringParam('CLUSTER', 'us-west-1 : dev23', '<region : cluster> value pair')
			booleanParam('HAS_HTTPS', false, 'enable HTTPS')
			stringParam('DUMP_ORIGIN', '', '')
			choiceParam('BACKUP_INTERVAL', ["WITHOUT", "5 minutes", "10 minutes", "30 minutes", "1 hour", "6 hours", "12 hours", "1 day", "15 days", "30 days"], 'Backup Interval')
			stringParam('DUMP_ORIGING_S3_URL', '', 'S3 URL of file dump. ex: s3://dump_folder/dump_file.zip')
			stringParam('SITE_TITLE', '', 'Site Title')
			stringParam('ADMIN_USER', '', 'Admin User')
			stringParam('PASS_ADMIN', '', 'Password for Admin user')
			stringParam('EMAIL', '', '')
			booleanParam('COMPOSER_UPDATE', false, '')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/drupal_devops')
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
			stringParam('SITE_ID', '', 'Cluster Name')
			stringParam('CLUSTER', 'us-west-1 : dev23', '<region : cluster> value pair')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/drupal_devops')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/delete/Jenkinsfile')
				lightweight = true
			}
		}
	}

	pipelineJob(jobNames[2]) {
		displayName(jobNames[2])
		description(jobNames[2])
		parameters {
			stringParam('CLUSTER', 'us-west-1 : dev23', '<region : cluster> value pair')
			stringParam('ORIGIN', 'devpaneldb_[schema]', '')
			stringParam('DESTINATION', 'devpaneldb_[schema]', '')
			stringParam('SITE_CNAME', 'drupal.devpanel.me', 'Site Name')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/drupal_devops')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/clone/Jenkinsfile')
				lightweight = true
			}
		}
	}

	pipelineJob(jobNames[3]) {
		displayName(jobNames[3])
		description(jobNames[3])
		parameters {
			stringParam('CLUSTER', 'us-west-1 : dev23', '<region : cluster> value pair')
			stringParam('DUMP_ORIGING_S3_URL', 's3://dump_folder/dump_file.zip', 'S3 URL of file dump. ex: s3://dump_folder')
			stringParam('CONTAINER_DB_NAME', 'devpaneldb_[schema]', 'Container DB Name')
			stringParam('SITE_CNAME', 'drupal.devpanel.me', 'Site Name')
		}
		definition {
			cpsScm {
				scm {
					git {
						remote {
							url('https://git-codecommit.us-west-1.amazonaws.com/v1/repos/drupal_devops')
							credentials('')
							branch('master')
						}
					}
				}
				scriptPath('devops/jenkins/jobs/pipeline/restore/Jenkinsfile')
				lightweight = true
			}
		}
	}

}

