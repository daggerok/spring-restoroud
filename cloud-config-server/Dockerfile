#FROM java:8
#MAINTAINER Maksim Kostromin <daggerok@gmail.com>
##ENV DOCKER_HOST 192.168.99.100
## spring boot produces logs, so we need /tmp for it
#VOLUME /tmp
## expose public port
#EXPOSE 8888
#ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar app.jar
## copy executable application fat jar as app.jar
##ADD daggerok-cloud-config-server-1.jar app.jar
#ADD build/libs/*.jar app.jar
FROM openjdk:8u171-jdk-alpine
LABEL MAINTAINER='Maksim Kostromin <daggerok@gmail.com>'
RUN apk --no-cache --update add bash curl
VOLUME /tmp
ENTRYPOINT java -jar ./app.jar
EXPOSE 8888
HEALTHCHECK --timeout=1s \
            --retries=66 \
            CMD curl -f http://127.0.0.1:8888/actuator/health || exit 1
ADD build/libs/*.jar ./app.jar
