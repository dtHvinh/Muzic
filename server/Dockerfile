FROM openjdk:26-ea-28-jdk-oraclelinux9
WORKDIR /app
COPY target/server-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
