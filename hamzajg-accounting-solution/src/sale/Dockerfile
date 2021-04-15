FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/sale/build/libs/sale-*-all.jar sale.jar
COPY src/sale/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf sale.jar xoom-turbo.properties
EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar sale.jar