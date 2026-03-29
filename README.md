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
- ActiveMQ (running on `localhost:61616`) or any AMQP 1.0 broker.

## Dependency Setup

### 1. OpenDaylight (ODL) Central Controller
The paper refers to ODL (Beryllium/Lithium). To run with a modern ODL (e.g., Chlorine):
1. Download ODL: `wget https://nexus.opendaylight.org/content/repositories/opendaylight.release/org/opendaylight/integration/karaf/0.17.3/karaf-0.17.3.tar.gz`
2. Extract and run: `./bin/karaf`
3. Install base features: `feature:install odl-restconf odl-mdsal-all`

### 2. Messaging4Transport Bundle
1. Clone the repo: `git clone https://github.com/KathiraveluLab/Messaging4Transport.git`
2. Build the project: `mvn clean install -DskipTests`
3. Load the feature in ODL:
   ```bash
   repo-add mvn:org.opendaylight.messaging4transport/messaging4transport-features/1.0.0-SNAPSHOT/xml/features
   feature:install odl-messaging4transport-impl
   ```

## How to Build Cassowary

```bash
mvn clean install
```

## How to Run

1. Start ActiveMQ.
2. Run the `CassowaryServer`:
   ```bash
   mvn -pl cassowary-core exec:java -Dexec.mainClass="org.cassowary.core.CassowaryServer"
   ```
3. Run the `BuildingSimulation`:
   ```bash
   mvn -pl cassowary-sim exec:java -Dexec.mainClass="org.cassowary.sim.BuildingSimulation"
   ```

## Citing Cassowary

If you use Cassowary in your research, please cite the below paper:

* Kathiravelu, P., Sharifi, L. and Veiga, L., 2015, December. **Cassowary: Middleware platform for context-aware smart buildings with software-defined sensor networks.** In Proceedings of the 2nd Workshop on Middleware for Context-Aware Applications in the IoT (pp. 1-6).
