#!/usr/bin/bash

cd ../../backend;
mvn clean verify -Ptest;
sleep 5s;
mvn clean;
cd ../scripts/lin;