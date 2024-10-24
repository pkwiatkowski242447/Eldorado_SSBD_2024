FROM mysql:9 AS db-stage

EXPOSE 3306

ENV MYSQL_ROOT_PASSWORD="password"

WORKDIR /docker-entrypoint-initdb.d

COPY backend/src/main/resources/sql/init_scripts/* .

#TODO czy to wgl dziala xD? Moze trzeba dodac --> --password=$MYSQL_ROOT_PASSWORD
HEALTHCHECK --interval=20s --timeout=20s --retries=3 \
CMD mysqladmin ping -h localhost || exit 1


FROM tomcat:11.0 AS tomcat-stage

EXPOSE 8080

WORKDIR /usr/local/tomcat/webapps
#todo very optional
#RUN apt-get update && apt-get install -y tzdata
#ENV TZ=Europe/Warsaw

#COPY backend/target/rest_application.war ./ROOT.war
#
HEALTHCHECK --interval=20s --timeout=20s --retries=15 --start-period=2m \
CMD curl -s --head localhost:8080/eldorado/ | head -1 | grep 200  || exit 1


FROM nginx:latest AS nginx-stage

EXPOSE 80 443

WORKDIR /etc/nginx

COPY nginx/conf/* ./conf.d
COPY nginx/ssl/* ./ssl

RUN chmod -R 666 ./conf.d && chmod -R 666 ./ssl