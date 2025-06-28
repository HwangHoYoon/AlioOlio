FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} alioolio-0.0.1-SNAPSHOT.jar
ENV TZ Asia/Seoul
ENTRYPOINT ["java", "-jar","/alioolio-0.0.1-SNAPSHOT.jar"]