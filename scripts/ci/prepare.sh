#!/usr/bin/env bash

function prepare_backend() {
    mvn -f ./nb-backend/ clean
    npm install -g yarn
}

function prepare_frontend() {
    cd nb-frontend/
    yarn install
    yarn add global codecov --cwd ./nb-frontend/
}
