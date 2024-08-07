FROM openjdk:17-jdk-alpine
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-Dapikey=secret", "-jar", "app.jar"]