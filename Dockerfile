FROM openjdk:8-jdk-alpine

ARG JAR_FILE=./presentation/target/*.jar
COPY ${JAR_FILE} app.jar

CMD ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]