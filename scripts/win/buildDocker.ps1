cd ../..;
docker build -f Dockerfile -t docker-db --target db-stage .;
docker build -f Dockerfile -t docker-tomcat --target tomcat-stage .;
docker build -f Dockerfile -t docker-nginx --target nginx-stage .;
cd scripts/win;