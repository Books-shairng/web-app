#!/usr/bin/env bash

. scripts/ci/prepare.sh
. scripts/ci/build.sh
. scripts/ci/docker.sh

function build_with_docker() {
    prepare_backend
    prepare_frontend
    build_services
    build_docker_images
}
