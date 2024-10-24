#!/usr/bin/bash

cd ../../backend;
mvn clean install -DskipTests -Ptest;
cd ../scripts/lin;