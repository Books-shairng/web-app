#!/usr/bin/env bash
mvn -f ./nb-backend/ test -q -Dlog4j.configurationFile=null
