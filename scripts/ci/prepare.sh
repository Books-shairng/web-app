#!/usr/bin/env bash

function prepare_backend() {
    echo "########## Start preparing backend ##########"

    mvn -f ./nb-backend/ clean

    echo "########## Done preparing backend ##########"
}

function prepare_frontend() {
    echo "########## Start preparing frontend ##########"

    npm install -g yarn
    yarn global add @angular/cli codecov
    yarn install --cwd ./nb-frontend/

    echo "########## Stop preparing fronted ##########"
}
