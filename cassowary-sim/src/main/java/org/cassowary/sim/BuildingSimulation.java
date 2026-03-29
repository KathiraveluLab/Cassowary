package org.cassowary.sim;

import org.cassowary.sim.device.MockAppliance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingSimulation {
    private static final Logger logger = LoggerFactory.getLogger(BuildingSimulation.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting Cassowary Building Simulation...");
        
        String brokerUrl = "tcp://localhost:61616";
        
        // 1. Initialize Server and Simulated App
        CassowaryServer server = new CassowaryServer(brokerUrl, "http://localhost:8181");
        server.start();

        // 2. Simulate Sensor Readings and Movement
        new Thread(() -> {
            try {
                logger.info("--- SCENARIO 1: Tenant in Close Proximity (QoS: Comfort + Energy) ---");
                // Mock a natural light reading first
                server.onSensorEvent(new ContextData("sensor-ext", "light_external", 40.0)); 

                ContextData t1 = new ContextData("hvac-01", "temperature", 28.0);
                ContextData l1 = new ContextData("light-01", "light", 10.0);
                ContextData d1 = new ContextData("display-01", "display", 1.0); // 1.0m distance
                
                server.onSensorEvent(t1);
                server.onSensorEvent(l1);
                server.onSensorEvent(d1);
                
                Thread.sleep(5000);
                
                logger.info("--- SCENARIO 2: Tenant Leaves the Area (QoS: Pollution Prevention + Energy Efficiency) ---");
                // Distance increases for display and light
                ContextData d2 = new ContextData("display-01", "display", 12.0); // 12m away
                ContextData l2 = new ContextData("light-01", "light", 15.0); // 15m away
                
                server.onSensorEvent(d2); // Should trigger hibernation (L=0)
                server.onSensorEvent(l2); // Should trigger dimming (L=0)
                
            } catch (Exception e) {
                logger.error("Simulation error", e);
            }
        }).start();

        logger.info("Simulation running. Press Ctrl+C to stop.");
    }
}
