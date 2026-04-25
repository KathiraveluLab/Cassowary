#!/bin/bash
set -e

echo "========================================================"
echo "          Cassowary Environment Setup Script           "
echo "========================================================"

# 1. Start ActiveMQ Broker
echo "[1/2] Starting ActiveMQ via Docker Compose..."
if command -v docker-compose &> /dev/null; then
    docker-compose up -d
else
    docker compose up -d
fi

# 2. Download and Setup OpenDaylight (ODL)
ODL_VERSION="0.17.3"
ODL_TAR="karaf-${ODL_VERSION}.tar.gz"
ODL_DIR="karaf-${ODL_VERSION}"

echo "[2/2] Setting up OpenDaylight (ODL) Controller..."
if [ ! -d "$ODL_DIR" ]; then
    echo "Downloading OpenDaylight Karaf ${ODL_VERSION}..."
    wget -qC - "https://nexus.opendaylight.org/content/repositories/opendaylight.release/org/opendaylight/integration/karaf/${ODL_VERSION}/${ODL_TAR}"
    
    echo "Extracting OpenDaylight..."
    tar -xzf "${ODL_TAR}"
    rm "${ODL_TAR}"
else
    echo "OpenDaylight directory '${ODL_DIR}' already exists. Skipping download."
fi

# Stop any running instance gracefully before starting
echo "Ensuring no previous ODL instances are hanging..."
./${ODL_DIR}/bin/stop 2>/dev/null || true
sleep 2

echo "Starting OpenDaylight as a background daemon..."
./${ODL_DIR}/bin/start

echo "Waiting for OpenDaylight Karaf SSH console to become available..."
# Wait for the Karaf SSH port (8101) to open
while ! nc -z localhost 8101 >/dev/null 2>&1; do
  sleep 2
done

# Give it a few extra seconds to fully initialize the feature manager
sleep 10

echo "Installing required ODL features (odl-restconf, odl-mdsal-all)..."
./${ODL_DIR}/bin/client -u karaf -p karaf "feature:install odl-restconf odl-mdsal-all"

# 3. Build Cassowary Project
echo "[3/3] Building Cassowary Project..."
mvn clean install -DskipTests

echo "========================================================"
echo "Setup Complete!"
echo ""
echo "- ActiveMQ Broker is accessible at amqp://localhost:5672"
echo "- OpenDaylight Controller is running in the background"
echo "- Cassowary project built successfully"
echo ""
echo "To stop OpenDaylight, run: ./${ODL_DIR}/bin/stop"
echo "To stop ActiveMQ, run: docker compose down"
echo "========================================================"
