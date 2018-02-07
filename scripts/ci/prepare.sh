#!/usr/bin/env bash

function prepare_backend() {
    mvn -f ./nb-backend/ clean
}

function prepare_frontend() {
    npm install -g yarn
    yarn install --cwd ./nb-backend/
    yarn add global codecov --cwd ./nb-frontend/
}
