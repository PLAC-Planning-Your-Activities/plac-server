FROM openjdk:11.0.16-jdk

COPY build/libs/plac-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

ENV TZ=Asia/Seoul
