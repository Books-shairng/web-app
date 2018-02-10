#!/usr/bin/env bash

. scripts/ci/test.sh
. scripts/ci/builder.sh

case $@ in
bk_unit_test)
   bk_ut
   ;;
bk_integration_test)
   bk_it
   ;;
build_services_with_docker)
   build_with_docker
   ;;
esac
