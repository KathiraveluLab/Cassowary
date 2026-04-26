package org.opendaylight.messaging4transport.impl;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.*;

public class Publisher {
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public Publisher(String brokerUrl) throws JMSException {
        JmsConnectionFactory factory = new JmsConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("event");
        producer = session.createProducer(destination);
        logger.info("Publisher initialized with AMQP broker at {}", brokerUrl);
    }

    public void publish(String messageText) throws JMSException {
        TextMessage message = session.createTextMessage(messageText);
        producer.send(message);
        logger.debug("Published message: {}", messageText);
    }

    public void close() throws JMSException {
        if (producer != null) producer.close();
        if (session != null) session.close();
        if (connection != null) connection.close();
    }
}
