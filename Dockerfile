FROM bellsoft/liberica-openjdk-centos:17-cds

LABEL org.opencontainers.image.source=https://github.com/anastasiosbroadcom/spring-boot-starter-web

COPY target/web-0.0.1-SNAPSHOT.jar target/web-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/target/web-0.0.1-SNAPSHOT.jar"]