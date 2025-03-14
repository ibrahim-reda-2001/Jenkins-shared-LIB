# Stage 1: Build the Maven project
FROM openjdk:17-jdk-slim AS build_image

# Install Maven
ARG MAVEN_VERSION=3.9.6
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /usr/local && \
    ln -s /usr/local/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/local/bin/mvn && \
    apt-get purge -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the entire project (pom.xml and src/)
COPY . .

# Build the project
RUN mvn clean package

# Stage 2: Create the runtime image
FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY --from=build_image /app/target/simple-maven-app-1.0-SNAPSHOT.jar app.jar

# Run the JAR
CMD ["java", "-jar", "app.jar"]