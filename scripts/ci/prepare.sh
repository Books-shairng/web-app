#!/usr/bin/env bash

function prepare_backend() {
    mvn -f ./nb-backend/ clean
}

function prepare_frontend() {
    npm install -g yarn
    yarn global add @angular/cli codecov
    yarn install --cwd ./nb-backend/
}
