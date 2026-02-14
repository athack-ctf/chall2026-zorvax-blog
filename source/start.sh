#!/bin/bash

# DB user and database
MYSQL_ROOT_PASSWORD="samsungsql"
MYSQL_DATABASE="jesterdb1"

# Start MySQL in the background
mysqld_safe &
MYSQL_PID=$!

# Wait for MySQL to be ready
until mysqladmin ping --silent; do
    echo "Waiting for MySQL..."
    sleep 2
done

echo "MySQL is up and running!"

# Set MySQL root password manually (since ENV variables don’t apply automatically)
mysql -uroot <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '${MYSQL_ROOT_PASSWORD}';
FLUSH PRIVILEGES;
EOF
echo "Root password has been set."

# Create the database if it doesn’t exist
mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" <<EOF
CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};
EOF
echo "Database ${MYSQL_DATABASE} created."

# Import SQL seed file if available
if [ -f /db/jesterdb1.sql ]; then
    echo "Seeding database sql file..."
    mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" ${MYSQL_DATABASE} < /db/jesterdb1.sql
    echo "Database seeding complete."
else
    echo "No SQL seed file found, skipping import."
fi

# Now start your application
echo "Starting jestersblog.jar..."
java -jar /app/jestersblog.jar
