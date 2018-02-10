#!/usr/bin/env bash

CI_SCRIPT_PATH=$TRAVIS_BUILD_DIR/scripts/ci

. $CI_SCRIPT_PATH/build.sh
. $CI_SCRIPT_PATH/docker.sh
. $CI_SCRIPT_PATH/prepare.sh

function build_with_docker() {
    prepare_backend
    prepare_frontend
    build_services
    build_docker_images
}
