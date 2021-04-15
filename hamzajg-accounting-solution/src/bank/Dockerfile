FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY src/bank/build/libs/bank-*-all.jar bank.jar
COPY src/bank/src/main/resources/xoom-turbo.docker.properties xoom-turbo.properties
RUN jar uf bank.jar xoom-turbo.properties
EXPOSE 18080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar bank.jar