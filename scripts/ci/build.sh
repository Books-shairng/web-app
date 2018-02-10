#!/usr/bin/env bash

function build_services() {
    echo "########## Build backend ##########"
    mvn -f ./nb-backend/ verify -P skip-tests -q -Dlog4j.configurationFile=null -DskipTests=true

    echo "########## Build frontend ##########"
    yarn --cwd ./nb-frontend/ build
}
