FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/assets/build/libs/assets-*-all.jar assets.jar
COPY src/assets/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf assets.jar xoom-turbo.properties

EXPOSE 28080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar assets.jar