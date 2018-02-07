#!/usr/bin/env bash

case $@ in
bk_unit_test)
    mvn -f ./nb-backend/ test -q -Dlog4j.configurationFile=null
   ;;
bk_integration_test)
    mvn -f ./nb-backend/ cobertura:cobertura-integration-test -q -Dlog4j.configurationFile=null
    ;;
esac
