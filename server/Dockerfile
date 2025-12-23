FROM openjdk:26-ea-28-jdk-oraclelinux9
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
