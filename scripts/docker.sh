#!/usr/bin/env bash

DOCKER_TAG_VERSION = $(<  ../version )

nb_services=(frontend backend)

if [$TRAVIS_BRANCH == 'develop'] || [$TRAVIS_BRANCH == 'feat-docker-config']; then
    for index in ${nb_services[*]}
    do
        echo "########## Build nb-$index docker image ##########"
        docker build -t nb-$index:${DOCKER_TAG_VERSION} ../nb-$index/
    done

fi
