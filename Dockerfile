FROM bellsoft/liberica-openjdk-centos:17-cds

LABEL org.opencontainers.image.source=https://github.com/anastasiosbroadcom/spring-boot-gateway-example

COPY target/gateway-demo-0.0.1-SNAPSHOT.jar target/gateway-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/target/gateway-demo-0.0.1-SNAPSHOT.jar"]