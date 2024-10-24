#!/usr/bin/bash

cd ../../backend;
mvn clean install -DskipTests -Pprod;
cd ../scripts/lin;