FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} kaddem-1.0.0.jar
ENTRYPOINT ["java", "-jar" ,"/kaddem-1.0.0.jar"]
EXPOSE 8089