FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/purchase/build/libs/purchase-*-all.jar purchase.jar
COPY src/purchase/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf purchase.jar xoom-turbo.properties
EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar purchase.jar