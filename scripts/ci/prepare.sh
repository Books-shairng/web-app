#!/usr/bin/env bash

mvn -f ./nb-backend/ clean
npm install -g yarn

cd nb-frontend/
yarm install
yarn add global codecov --cwd ./nb-frontend/
