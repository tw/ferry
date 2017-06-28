FROM openjdk:8-jre-alpine

MAINTAINER Tobie Warburton "tobie.warburton@gmail.com"

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

COPY conf/default.yml /app/conf/
COPY target/ferry-standalone.jar /app/
WORKDIR /app

EXPOSE 3000

CMD ["java", "-jar", "ferry-standalone.jar", "server", "conf/default.yml"]
