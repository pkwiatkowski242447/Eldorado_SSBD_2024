#!/usr/bin/bash

docker run --name db -p 3306:3306 --detach --rm docker-db;
docker run --name tomcat -p 8080:8080 --detach --rm docker-tomcat;
docker run --name nginx -p 80:80 -p 443:443 --rm docker-nginx;