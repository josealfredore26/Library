# Use an official Maven image as the base image
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package -DskipTests


# Use the OpenJDK image to run the application
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the build stage to the current directory in the container
COPY --from=build /app/target/Library-0.0.1-SNAPSHOT.jar .

# Expose port 8080 to the outside world
EXPOSE 8080

# Define the entry point for the container, specifying how to run the application
ENTRYPOINT ["java", "-jar", "Library-0.0.1-SNAPSHOT.jar"]
