package org.cassowary.network.mom;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.jms.*;

public class AmqpBrokerClient {
    private static final Logger logger = LoggerFactory.getLogger(AmqpBrokerClient.class);
    private Connection connection;
    private Session session;
    private final String url;

    public AmqpBrokerClient(String url) {
        this.url = url;
    }

    public void start() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        logger.info("Connected to AMQP Broker at {}", url);
    }

    public void publish(String topicName, String messageText) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(topic);
        TextMessage message = session.createTextMessage(messageText);
        producer.send(message);
        producer.close();
    }

    public void subscribe(String topicName, MessageListener listener) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(listener);
        logger.info("Subscribed to topic: {}", topicName);
    }

    public void stop() throws JMSException {
        if (session != null) session.close();
        if (connection != null) connection.stop();
    }
}
