
# Stage 1: Build the application
FROM maven:3.8.5-openjdk-11 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
RUN ping -c 4 repo.maven.apache.org || echo "Maven Central not reachable"

# Stage 2: Run the application
FROM openjdk:11
WORKDIR /app
COPY --from=builder /app/target/kaddem-0.0.1-SNAPSHOT.jar kaddem-0.0.1-SNAPSHOT.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "kaddem-0.0.1-SNAPSHOT.jar"]
