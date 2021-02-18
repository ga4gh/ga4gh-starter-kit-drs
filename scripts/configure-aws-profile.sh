#!/bin/bash
PROFILE=ga4gh-serviceaccount-basic

aws configure set aws_access_key_id     $AWS_PUBLIC_KEY  --profile $PROFILE
aws configure set aws_secret_access_key $AWS_SECRET_KEY  --profile $PROFILE
