# Use an OpenJDK image as the base
FROM openjdk:11

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/kaddem-0.0.1-SNAPSHOT.jar app.jar 

# Expose the application port
EXPOSE 8090

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
