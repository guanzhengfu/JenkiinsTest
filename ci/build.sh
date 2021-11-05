#!/usr/bin/env bash
set +x

./gradlew clean compileJava compileTestJava bootRepackage --info
