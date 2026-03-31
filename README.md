# Cassowary Middleware Platform for Software-Defined Buildings

Implementation of the Cassowary middleware for Context-Aware Smart Buildings with Software-Defined Sensor Networks.

## Project Structure

- `cassowary-common`: Shared data models (Profile, ContextData) and Policy engine (Equations 1 & 3).
- `cassowary-appliance`: Infinispan IMDG cluster for distributed profile/policy storage.
- `cassowary-network`: AMQP (ActiveMQ) and SDN (OpenDaylight) integration layer.
- `cassowary-core`: Main orchestrator and SDSN control loop.
- `cassowary-sim`: Simulation environment using CloudSim.

## Prerequisites

- Java 17+ (Project built for Java 17)
- Maven 3.6+
- Docker & Docker Compose (for running ActiveMQ)

## Dependency Setup

### 1. ActiveMQ Broker (AMQP)
To run the pub/sub messaging layer, start the broker using Docker:
```bash
docker compose up -d
```
The broker will be available at `amqp://localhost:5672`.

### 2. OpenDaylight (ODL) Central Controller
The project uses RESTCONF to interact with ODL. To run with a modern ODL (e.g., Chlorine):
1. Download [ODL Karaf](https://nexus.opendaylight.org/content/repositories/opendaylight.release/org/opendaylight/integration/karaf/0.17.3/karaf-0.17.3.tar.gz).
2. Extract and run: `./bin/karaf`
3. Install base features: `feature:install odl-restconf odl-mdsal-all`

## Context-Aware Policy Engine
The middleware implements a context-aware policy engine that handles jitter and external environment factors (e.g., natural light intensity `Ls`). It dynamically calculates comfort and energy efficiency targets based on tenant profiles.

## How to Build & Run

### 1. Build the project
```bash
mvn clean install -DskipTests
```

### 2. Run the `BuildingSimulation`
The simulation demonstrates Scenarios 1 and 2 from the research paper, covering tenant proximity and automatic actuation:
```bash
mvn -pl cassowary-sim exec:java -Dexec.mainClass="org.cassowary.sim.BuildingSimulation"
```

## Citing Cassowary

If you use Cassowary in your research, please cite the below paper:

* Kathiravelu, P., Sharifi, L. and Veiga, L., 2015, December. **Cassowary: Middleware platform for context-aware smart buildings with software-defined sensor networks.** In Proceedings of the 2nd Workshop on Middleware for Context-Aware Applications in the IoT (pp. 1-6).
