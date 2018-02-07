#!/usr/bin/env bash

source build.sh
source docker.sh
source prepare.sh

prepare_frontend
prepare_backend
build_services
build_docker_images
