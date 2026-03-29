package org.cassowary.core;

import org.cassowary.appliance.grid.InfinispanGrid;
import org.cassowary.appliance.store.ConfigDataStore;
import org.cassowary.common.model.ContextData;
import org.cassowary.common.model.Profile;
import org.cassowary.common.policy.CassowaryPolicy;
import org.cassowary.network.mom.AmqpBrokerClient;
import org.cassowary.network.sdn.SdnControllerClient;
import org.cassowary.core.context.ContextHistory;
import org.opendaylight.messaging4transport.impl.Messaging4TransportDataListener;
import org.opendaylight.messaging4transport.impl.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class CassowaryServer {
    private static final Logger logger = LoggerFactory.getLogger(CassowaryServer.class);
    
    private final InfinispanGrid grid;
    private final Publisher publisher;
    private final SdnControllerClient sdn;
    private final ConfigDataStore store;
    private final CassowaryPolicy policyEngine;
    private final Messaging4TransportDataListener dataListener;
    private final ContextHistory contextHistory;

    public CassowaryServer(String brokerUrl, String sdnUrl) throws Exception {
        this.grid = new InfinispanGrid();
        this.publisher = new Publisher(brokerUrl);
        this.sdn = new SdnControllerClient(sdnUrl);
        this.store = new ConfigDataStore(grid);
        this.policyEngine = new CassowaryPolicy();
        this.dataListener = new Messaging4TransportDataListener(publisher);
        this.contextHistory = new ContextHistory();
    }

    public void start() throws Exception {
        logger.info("Cassowary Server Started.");
    }

    public void onSensorEvent(ContextData data) {
        logger.debug("Processing sensor event: {}", data.getDeviceId());
        
        // Add to history for jitter mitigation
        contextHistory.addValue(data.getDeviceId() + "_" + data.getType(), data.getValue());
        Double averagedValue = contextHistory.getAverage(data.getDeviceId() + "_" + data.getType());
        
        ContextData averagedData = new ContextData(data.getDeviceId(), data.getType(), averagedValue);
        sdn.updateDataTree(averagedData);
        dataListener.onDataChanged(averagedData);
        
        // Trigger actuation calculation
        calculateAndActuate(averagedData);
    }

    private void calculateAndActuate(ContextData data) throws Exception {
        // Fetch profiles from building-specific grid
        String currentBuilding = "building-1"; 
        Profile p1 = store.getProfile(currentBuilding, "tenant-1");
        if (p1 == null) {
            p1 = new Profile("tenant-1", "Default Tenant");
            p1.getPreferences().put("temperature", 22.0);
            store.saveProfile(currentBuilding, p1);
        }
        
        // Get natural light Ls if calculating illumination
        if ("light".equalsIgnoreCase(data.getType())) {
            Double Ls = contextHistory.getAverage("sensor_light_external");
            if (Ls == null) Ls = 50.0; // fallback per paper
            data.setValue(Ls); // Pass Ls as additional context in a real implementation
        }

        Double targetValue = policyEngine.calculate(data.getType(), 
            Collections.singletonList(p1), Collections.singletonList(data.getValue()));
            
        logger.info("Calculated target for {}: {} (Averaged)", data.getType(), targetValue);
        
        // Publish actuation command
        try {
            publisher.publish("SET " + data.getType() + " TO " + targetValue);
        } catch (Exception e) {
            logger.error("Error publishing actuation", e);
        }
    }

    public void stop() throws Exception {
        publisher.close();
        grid.stop();
    }

    public static void main(String[] args) throws Exception {
        CassowaryServer server = new CassowaryServer("tcp://localhost:61616", "http://localhost:8181");
        server.start();
    }
}
