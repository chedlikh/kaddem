# Use an official Java runtime as a parent image
FROM openjdk:11

# Set the working directory
WORKDIR /app

# Copy the built artifact from the Maven build stage
COPY target/kaddem-${BUILD_NUMBER}.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
