# ecs-jenkins
Standalone, out-of-the-box setup of infra for dockerized Jenkins master and slaves. Inspired by the work of odavid/my-bloody-jenkins.


# Deploying a stack:

1. `sync.sh <bucket-name> <profile for AWS credentials>` - uploads needed files to S3 bucket, from which deployment of a stack will be done.
2. `cd deploy`
3. `aws cloudformation deploy --profile devpanel --region us-west-1 --stack-name jenkins-ek-ecs --capabilities CAPABILITY_NAMED_IAM --no-fail-on-empty-changeset --template-file ./jenkins.yaml --parameter-overrides "S3Bucket=<bucket-name>" "KeyName=<ssh-key-name>" "HostedZoneId=<hosted-zone-id>" "SiteName=<jenkins-master-hostname>"`

Admin password for Jenkins master is available in `Outputs` of a CFN stack.
