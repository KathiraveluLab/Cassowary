package org.cassowary.sim.device;

import org.cassowary.network.mom.AmqpBrokerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

public class MockAppliance {
    private static final Logger logger = LoggerFactory.getLogger(MockAppliance.class);
    private final String id;
    private final String type;
    private final AmqpBrokerClient broker;

    public MockAppliance(String id, String type, String brokerUrl) {
        this.id = id;
        this.type = type;
        this.broker = new AmqpBrokerClient(brokerUrl);
    }

    public void start() throws JMSException {
        broker.start();
        // Subscribe to actuation updates
        broker.subscribe("cassowary.actuators." + id, message -> {
            try {
                if (message instanceof TextMessage) {
                    logger.info("APPLIANCE [{}]: Received Actuation: {}", id, ((TextMessage) message).getText());
                }
            } catch (JMSException e) {
                logger.error("Error receiving actuation", e);
            }
        });
    }

    public void sense(Double value) throws JMSException {
        String data = "{\"id\":\"" + id + "\", \"type\":\"" + type + "\", \"value\":" + value + "}";
        broker.publish("cassowary.sensor." + type + "." + id, data);
        logger.info("APPLIANCE [{}]: Sensed {} = {}", id, type, value);
    }

    public void stop() throws JMSException {
        broker.stop();
    }
}
