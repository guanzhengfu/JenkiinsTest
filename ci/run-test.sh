#!/usr/bin/env bash
set -e

./gradlew clean test -Dspring.profiles.active=test --info