FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/customer/build/libs/customer-*-all.jar customer.jar
COPY src/customer/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf customer.jar xoom-turbo.properties

EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar customer.jar