#!/bin/bash
set -e

echo "========================================================"
echo "          Cassowary Environment Setup Script           "
echo "========================================================"

# 1. Start ActiveMQ and OpenDaylight via Docker Compose
echo "[1/2] Starting ActiveMQ and OpenDaylight via Docker Compose..."
if command -v docker-compose &> /dev/null; then
    docker-compose up -d
else
    docker compose up -d
fi

echo "Waiting for OpenDaylight RESTCONF (port 8181) to become available (timeout: 300s)..."
ODL_WAIT=0
ODL_TIMEOUT=300
until curl -sf -u admin:admin -o /dev/null http://localhost:8181/restconf/config >/dev/null 2>&1 || \
      curl -sf -u admin:admin -o /dev/null -w "%{http_code}" http://localhost:8181/restconf/config 2>/dev/null | grep -qE "^[0-9]"; do
  if [ "$ODL_WAIT" -ge "$ODL_TIMEOUT" ]; then
    echo "ERROR: OpenDaylight did not become ready within ${ODL_TIMEOUT} seconds."
    echo "Check container logs with: docker logs cassowary-odl"
    exit 1
  fi
  sleep 5
  ODL_WAIT=$((ODL_WAIT + 5))
done
echo "OpenDaylight is up."


# 2. Build Cassowary Project
echo "[2/2] Building Cassowary Project..."
mvn clean install -DskipTests

echo "========================================================"
echo "Setup Complete!"
echo ""
echo "- ActiveMQ Broker is accessible at amqp://localhost:5672"
echo "- OpenDaylight RESTCONF is accessible at http://localhost:8181/restconf"
echo "- Cassowary project built successfully"
echo ""
echo "To stop all services, run: docker compose down"
echo "========================================================"
