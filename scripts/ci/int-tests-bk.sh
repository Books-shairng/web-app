#!/usr/bin/env bash
mvn -f ./nb-backend/ cobertura:cobertura-integration-test -q -Dlog4j.configurationFile=null
