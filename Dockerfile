# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app

# Copy pom.xml first to download dependencies (improves build caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Switch to the non-root user
USER appuser:appgroup

# Expose the application port
EXPOSE 8080

# ---------------------------------------------------------
# EXTERNAL CONFIGURATION (Database Connection)
# ---------------------------------------------------------
# You can pass Environment Variables to configure the database connection
# for different environments (local, staging, production):
#
# SPRING_DATASOURCE_URL
# SPRING_DATASOURCE_USERNAME
# SPRING_DATASOURCE_PASSWORD
#
# Example run command:
# docker run -p 8080:8080 \
#   -e SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/vitagest \
#   -e SPRING_DATASOURCE_USERNAME=myuser \
#   -e SPRING_DATASOURCE_PASSWORD=mypassword \
#   vitagest-backend
# ---------------------------------------------------------

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]