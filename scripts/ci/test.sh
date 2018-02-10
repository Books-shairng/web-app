#!/usr/bin/env bash

function bk_ut() {
    echo "############# Run backend unit tests #############"
    mvn clean -f ./nb-backend/ test -q -Dlog4j.configurationFile=null
    echo "############# Stop backend unit tests #############"
}

function bk_it() {
    echo "############# Run backend integration tests #############"
    mvn clean -f ./nb-backend/ cobertura:cobertura-integration-test -q -Dlog4j.configurationFile=null
    echo "############# Stop backend integration tests #############"
}
