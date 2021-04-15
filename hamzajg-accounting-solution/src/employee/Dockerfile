FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/employee/build/libs/employee-*-all.jar employee.jar
COPY src/employee/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf employee.jar xoom-turbo.properties

EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar employee.jar