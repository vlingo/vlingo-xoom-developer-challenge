FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/rental/build/libs/rental-*-all.jar rental.jar
COPY src/rental/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf rental.jar xoom-turbo.properties

EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar rental.jar