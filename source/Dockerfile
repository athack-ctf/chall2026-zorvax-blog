# --- Stage 1: Build ---
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Install binutils to provide 'objcopy' for jlink
RUN apt-get update && apt-get install -y binutils && rm -rf /var/lib/apt/lists/*

# Create a custom minimal JRE
RUN jlink \
    --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /javaruntime

COPY web-app/pom.xml .
RUN mvn dependency:go-offline -B
COPY web-app/src ./src
RUN mvn clean package -DskipTests



FROM eclipse-temurin:17-jre-alpine AS runtime

# Install MySQL and clean up in a single layer to minimize footprint
# Using Alpine packages here for maximum size reduction
RUN apk add --no-cache mysql mysql-client bash

COPY my.cnf /etc/my.cnf

# Setup MySQL directories
RUN mysql_install_db --user=mysql --datadir=/var/lib/mysql

WORKDIR /app
COPY --from=build /app/target/*.jar jestersblog.jar
COPY db/jesterdb1.sql /db/jesterdb1.sql

#################################################


EXPOSE 3306





########################################################################################################################
# 2 - Getting jesterblog.jar
########################################################################################################################

# Set the working directory inside the container
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar jestersblog.jar

# Expose the port the app runs on
EXPOSE 8080

########################################################################################################################
# 3 - Start mysql database and the jar
########################################################################################################################

# Copy the shell script into the container
COPY start.sh /start.sh

# Make the script executable
RUN chmod +x /start.sh


# Set the shell script as the entrypoint to start both services
ENTRYPOINT ["/start.sh"]

