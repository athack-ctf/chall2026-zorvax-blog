########################################################################################################################
# Building jestersblog.jar
########################################################################################################################

# Use official maven image to build the project
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY web-app/pom.xml ./

# Download dependencies (if they are not already cached in the local Maven repository)
RUN mvn dependency:resolve

# Copy the rest of the project files
COPY web-app/src ./src

# Build the application
RUN mvn clean install -DskipTests

########################################################################################################################
# Deploying jestersblog.jar with a mysql database
########################################################################################################################

# Use OpenJDK image for the runtime environment
FROM openjdk:17-jdk-slim

########################################################################################################################
# 1 - Installing mysql server
########################################################################################################################

# Set the environment variable to avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Update package list and install necessary dependencies
RUN apt update && \
    apt install -y wget lsb-release gnupg && \
    wget -O /tmp/mysql-apt-config.deb https://repo.mysql.com//mysql-apt-config_0.8.36-1_all.deb && \
    dpkg -i /tmp/mysql-apt-config.deb && \
    apt update && \
    apt install -y mysql-server && \
    rm -rf /var/lib/apt/lists/* /tmp/mysql-apt-config.deb

# Copy the SQL seed file into the container
COPY db/jesterdb1.sql /db/jesterdb1.sql

# Expose MySQL port
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

