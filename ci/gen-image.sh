#!/usr/bin/env bash
set +x

LOCAL_TAG=twuc-$TEAM:$SERVICE-$BUILD_NUMBER
REMOTE_TAG=$ECR_HOST/$LOCAL_TAG

username=$(cat /etc/docker-registry/username)
password=$(cat /etc/docker-registry/password)
endpoint=$(cat /etc/docker-registry/endpoint)

sudo docker login -u $username -p $password $endpoint

sudo docker build -t $LOCAL_TAG -f ci/Dockerfile .
sudo docker tag $LOCAL_TAG $REMOTE_TAG
sudo docker push $REMOTE_TAG
