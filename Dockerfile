FROM maven:3.9.8-amazoncorretto-21-al2023 as stage1

LABEL org.opencontainers.image.source=https://github.com/anastasiosbroadcom/spring-boot-starter-web

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"
# set working directory
WORKDIR /opt/demo
# copy just pom.xml
COPY pom.xml .
# go-offline using the pom.xml
RUN mvn dependency:go-offline
# copy your other files
COPY ./src ./src
# compile the source code and package it in a jar file
RUN mvn clean install -Dmaven.test.skip=true

FROM bellsoft/liberica-openjdk-centos:21-cds

WORKDIR /opt/demo
# copy over the built artifact from the maven image
COPY --from=stage1 /opt/demo/target/web-0.0.1-SNAPSHOT.jar /opt/demo/web-0.0.1-SNAPSHOT.jar

EXPOSE 8080

#COPY target/web-0.0.1-SNAPSHOT.jar target/web-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/opt/demo/web-0.0.1-SNAPSHOT.jar"]