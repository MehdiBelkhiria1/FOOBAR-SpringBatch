# Start from a lightweight base JDK image
FROM eclipse-temurin:17-jdk-alpine as builder

# Create a directory for the app
WORKDIR /app

# Copy the JAR file from the host into the container
COPY target/FOOBAR-SpringBatch-0.0.1-SNAPSHOT.jar /app/FOOBAR-SpringBatch-0.0.1-SNAPSHOT.jar

# Expose the port your app listens on (optional)
EXPOSE 9090

# Run the JAR
ENTRYPOINT ["java", "-jar", "FOOBAR-SpringBatch-0.0.1-SNAPSHOT.jar"]
