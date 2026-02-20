#!/bin/bash

MYSQL_ROOT_PASSWORD="samsungsql"
MYSQL_DATABASE="jesterdb1"

# 1. Critical for Alpine: Setup directories
mkdir -p /run/mysqld
chown -R mysql:mysql /run/mysqld
chown -R mysql:mysql /var/lib/mysql

# 2. Initialize the system tables if they don't exist
if [ ! -d "/var/lib/mysql/mysql" ]; then
    echo "Initializing MariaDB system tables..."
    mysql_install_db --user=mysql --datadir=/var/lib/mysql > /dev/null
fi

# 3. Start MariaDB with networking forced ON
mariadbd --user=mysql --bind-address=127.0.0.1 --skip-networking=OFF &

# 4. Wait for readiness
echo "Waiting for MariaDB..."
MAX_WAIT=30
while ! mariadb-admin ping --silent; do
    sleep 1
    ((MAX_WAIT--))
    if [ $MAX_WAIT -le 0 ]; then
        echo "MariaDB failed to start. Printing logs:"
        tail -n 20 /var/lib/mysql/*.err
        exit 1
    fi
done

# 5. Setup DB (Using 'localhost' for the grant)
mariadb -u root <<EOF
CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\`;
ALTER USER 'root'@'localhost' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}';
FLUSH PRIVILEGES;
EOF

# 6. Import data
if [ -f /db/jesterdb1.sql ]; then
    mariadb -u root -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < /db/jesterdb1.sql
fi

echo "Database ready. Starting Java App..."

# Use exec so Java catches signals

exec java -Xms64m -Xmx96m \
     -XX:MaxMetaspaceSize=96m \
     -Xss512k \
     -XX:+UseSerialGC \
     -XX:+OptimizeStringConcat \
     -XX:TieredStopAtLevel=1 \
     -XX:+UseStringDeduplication \
     -jar /app/jestersblog.jar