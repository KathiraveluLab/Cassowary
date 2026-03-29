package org.opendaylight.messaging4transport.impl;

import org.cassowary.common.model.ContextData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps ODL MD-SAL Data Tree changes to AMQP messages.
 */
public class Messaging4TransportDataListener {
    private static final Logger logger = LoggerFactory.getLogger(Messaging4TransportDataListener.class);
    private final Publisher publisher;

    public Messaging4TransportDataListener(Publisher publisher) {
        this.publisher = publisher;
    }

    public void onDataChanged(ContextData data) {
        try {
            String json = String.format("{\"deviceId\":\"%s\", \"type\":\"%s\", \"value\":%f}",
                data.getDeviceId(), data.getType(), data.getValue());
            publisher.publish(json);
        } catch (Exception e) {
            logger.error("Error publishing data change", e);
        }
    }
}
