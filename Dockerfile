FROM adoptopenjdk/openjdk11:alpine-jre
LABEL maintainer="sharapov.team@yandex.ru"
WORKDIR /currency-service
COPY target/currency-service-0.0.1-SNAPSHOT.jar /currency-service/currency-service.jar
COPY src/main/java/com/thewolf/currencyservice/currency-pairs.json /currency-service/currency-pairs.json
ENTRYPOINT ["java","-jar","currency-service.jar"]