listView('Self-Service') {
	
	String[] jobNames = ["build-jenkins-images"];
	def gitUrl = 'https://github.com/IevgenKabanets/ecs-jenkins.git'


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

	def buildPush = '''
	$(aws ecr get-login --no-include-email --region us-west-1)
	docker build -t 460520929798.dkr.ecr.us-west-1.amazonaws.com/jenkinsci:latest .
	docker build -t 460520929798.dkr.ecr.us-west-1.amazonaws.com/jenkinsci-slave:latest -f Dockerfile.slave .
	docker push 460520929798.dkr.ecr.us-west-1.amazonaws.com/jenkinsci:latest
	docker push 460520929798.dkr.ecr.us-west-1.amazonaws.com/jenkinsci-slave:latest
	'''

	job(jobNames[0]) {

		scm {
        	git(gitUrl)
    	}
		
		triggers {
	        scm('H 0 * * *')
	    }

	    steps {
	        shell(buildPush)
	    }
	}	
}

