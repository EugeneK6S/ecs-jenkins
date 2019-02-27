#!/bin/bash

BUCKET=$1
PROFILE=$2

aws s3 cp config.yml s3://$BUCKET/config.yml --profile $PROFILE
aws s3 sync util/ s3://$BUCKET/util/ --profile $PROFILE
aws s3 sync deploy/. s3://$BUCKET/ --profile $PROFILE
