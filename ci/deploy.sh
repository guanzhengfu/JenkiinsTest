#!/usr/bin/env bash
set +x
LOCAL_TAG=twuc-$TEAM:$SERVICE-$BUILD_NUMBER
REMOTE_TAG=$ECR_HOST/$LOCAL_TAG
PROFILES=$1
NAMESPACE="$TEAM-$PROFILES"

sed "s#{{profiles}}#$PROFILES#g; s#{{team}}#$TEAM#g; s#{{image}}#$REMOTE_TAG#g; s#{{service}}#$SERVICE#g; s#{{namespace}}#$NAMESPACE#g" ci/kube.yaml

sed "s#{{profiles}}#$PROFILES#g; s#{{team}}#$TEAM#g; s#{{image}}#$REMOTE_TAG#g; s#{{service}}#$SERVICE#g; s#{{namespace}}#$NAMESPACE#g" ci/kube.yaml | sudo kubectl --kubeconfig $KUBE_CONFIG apply -f -
