#!/usr/bin/env bash

function build_services() {
    echo "########## Build backend ##########"
    mvn -f ./nb-backend/ verify -q -Dlog4j.configurationFile=null -DskipTests=true

    echo "########## Build frontend ##########"
    yarn run build --cwd ./nb-frontend/
}
