#!/usr/bin/env bash

CI_SCRIPT_PATH=$TRAVIS_BUILD_DIR/scripts/ci

source $CI_SCRIPT_PATH/build.sh
source $CI_SCRIPT_PATH/docker.sh
source $CI_SCRIPT_PATH/prepare.sh

prepare_backend
prepare_frontend
build_services
build_docker_images
