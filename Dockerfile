FROM openjdk:8-jdk-alpine

RUN apk add --no-cache wget

ARG JAR_FILE=./presentation/target/*.jar
COPY ${JAR_FILE} app.jar

# wait for elasticsearch for auto creating indexes
COPY ./wait_for_elasticsearch.sh wait_for_elasticsearch.sh
RUN chmod +x wait_for_elasticsearch.sh

ENTRYPOINT ["/wait_for_elasticsearch.sh"]

CMD ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]