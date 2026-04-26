# Cassowary Middleware Platform for Software-Defined Buildings

Implementation of the Cassowary middleware for Context-Aware Smart Buildings with Software-Defined Sensor Networks.

## Project Structure

- `cassowary-common`: Shared data models (Profile, ContextData) and Policy engine (Equations 1 & 3).
- `cassowary-appliance`: Infinispan IMDG cluster for distributed profile/policy storage.
- `cassowary-network`: AMQP (ActiveMQ) and SDN (OpenDaylight) integration layer.
- `cassowary-core`: Main orchestrator and SDSN control loop.
- `cassowary-sim`: Simulation environment using CloudSim.

## Prerequisites

- Java 17+ and Maven 3.6+ (for building Cassowary)
- Docker & Docker Compose (for running ActiveMQ and OpenDaylight)

## Setup & Build

Both the ActiveMQ Broker (AMQP) and the OpenDaylight (ODL) Central Controller can be installed, and the project can be built, using the provided setup script. 

To configure the environment and build the project, run:
```bash
chmod +x setup_env.sh
./setup_env.sh
```

This script will automatically:
1. Start the **ActiveMQ** broker and **OpenDaylight** controller as Docker containers.
2. Wait for the ODL RESTCONF API to become available (`http://localhost:8181/restconf`).
3. Install the necessary RESTCONF and MD-SAL features inside the ODL container.
4. Build the Cassowary Maven project.

To stop all services afterwards:
```bash
docker compose down
```

## Context-Aware Policy Engine
The middleware implements a context-aware policy engine that handles jitter and external environment factors (e.g., natural light intensity `Ls`). It dynamically calculates comfort and energy efficiency targets based on tenant profiles.

## How to Run

### Run the `BuildingSimulation`
The simulation demonstrates Scenarios 1 and 2 from the research paper, covering tenant proximity and automatic actuation:
```bash
mvn -pl cassowary-sim exec:java -Dexec.mainClass="org.cassowary.sim.BuildingSimulation"
```

## Citing Cassowary

If you use Cassowary in your research, please cite the below paper:

* Kathiravelu, P., Sharifi, L. and Veiga, L., 2015, December. **Cassowary: Middleware platform for context-aware smart buildings with software-defined sensor networks.** In Proceedings of the 2nd Workshop on Middleware for Context-Aware Applications in the IoT (pp. 1-6).
