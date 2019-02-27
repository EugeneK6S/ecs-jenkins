listView('Others') {
	
	String[] jobNames = ["backup-s3"];


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

	def backupS3 = '''
	# Variables
	S3_FOLDER=devpanel-jenkins-backup
	S3_FILE=jenkins-configuration.tar

	# Create a directory for the job definitions 
	mkdir -p jenkins-config/jobs 

	# Copy global configuration files into the workspace 
	cp $JENKINS_HOME/*.xml jenkins-config/

	# Copy keys and secrets into the workspace 
	cp $JENKINS_HOME/identity.key.enc jenkins-config/ 
	cp $JENKINS_HOME/secret.key jenkins-config/ 
	cp $JENKINS_HOME/secret.key.not-so-secret jenkins-config/ 
	cp -r $JENKINS_HOME/secrets jenkins-config/ 

	# Copy user configuration files into the workspace 
	cp -r $JENKINS_HOME/users jenkins-config/ 

	# Copy custom Pipeline workflow libraries 
	cp -r $JENKINS_HOME/workflow-libs jenkins-config 

	# Copy job definitions into the workspace 
	rsync -am --include=\'config.xml\' --include=\'*/\' --prune-empty-dirs --exclude=\'*\' $JENKINS_HOME/jobs/ jenkins-config/jobs/ 

	# Goes to tar path 
	cd jenkins-config/ 

	# Create an archive from all copied files (since the S3 plugin cannot copy folders recursively) 
	tar -cf $S3_FILE * 

	# Upload archive to S3 
	aws s3 cp $S3_FILE s3://$S3_FOLDER 

	# Remove the directory so only the tar gets copied to S3 
	rm -rf ../*
	'''

	job(jobNames[0]) {
		
		triggers {
	        cron('H 0 * * *')
	    }

	    steps {
	        shell(backupS3)
	    }
	}	
}

