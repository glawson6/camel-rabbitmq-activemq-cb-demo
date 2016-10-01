package com.taptech.ttis.camel;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tap on 9/29/16.
 */
@Configuration
@EnableConfigurationProperties({AmqpServerProperties.class})
@ConditionalOnProperty(name = "camel.enabled", havingValue = "true")
public class CamelConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CamelConfiguration.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    CamelContext camelContext;

    @Autowired
    AmqpServerProperties amqpServerProperties;


    private String createEndpointURI(AmqpServerProperties amqpServerProperties, String routingKey, String queueName) {
        StringBuilder sb = new StringBuilder(amqpServerProperties.serverURI());
        sb.append("/notification.event.exchange?routingKey=").append(routingKey);
        sb.append("&username=").append(amqpServerProperties.getUsername());
        sb.append("&password=").append(amqpServerProperties.getPassword());
        sb.append("&vhost=").append(amqpServerProperties.getVirtualHost());
        sb.append("&requestedHeartbeat=").append(amqpServerProperties.getHeartbeat());
        sb.append("&queue=").append(queueName);
        sb.append("&").append("concurrentConsumers=").append(amqpServerProperties.getConcurrentConsumers());
        return sb.toString();
    }

    @Bean(name = "rabbitEndpoint")
    Endpoint notificationEventEndpoint() {
        String endpointURI = createEndpointURI(amqpServerProperties, "ttis.event", "ttis.event.queue");
        Endpoint notificationEventEndpoint = camelContext.getEndpoint(endpointURI);
        return notificationEventEndpoint;
    }

    @Bean(name = "ttisEventExchange")
    DirectExchange notificationEventExchange() {
        return new DirectExchange("ttis.event.exchange", true, false);
    }

    @Bean(name = "ttisEventQueue")
    public Queue notificationEventQueue() {
        return new Queue("ttis.event.queue", true);
    }

    @Bean
    Binding notificationEventExchangeBinding(@Qualifier("ttisEventExchange") DirectExchange notificationEventExchange, @Qualifier("ttisEventQueue") Queue notificationEventQueue) {
        return BindingBuilder.bind(notificationEventQueue).to(notificationEventExchange).with("ttis.event");
    }

    /*
    @Bean(name="jmsConnectionFactory")
    ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        return connectionFactory;
    }
    */

    @Bean(name="pooledConnectionFactory")
    PooledConnectionFactory pooledConnectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");

        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
        pooledConnectionFactory.setMaxConnections(8);
        pooledConnectionFactory.setConnectionFactory(connectionFactory);
        return pooledConnectionFactory;
    }

    @Bean(name="jmsConfig")
    JmsConfiguration jmsConfiguration(@Qualifier("pooledConnectionFactory")PooledConnectionFactory pooledConnectionFactory){
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setConnectionFactory(pooledConnectionFactory);
        jmsConfiguration.setConcurrentConsumers(5);
        return jmsConfiguration;
    }

    @Bean(name="activemq")
    ActiveMQComponent activeMQComponent(@Qualifier("jmsConfig")JmsConfiguration jmsConfiguration){
        ActiveMQComponent activeMQComponent = new ActiveMQComponent();
        activeMQComponent.setConfiguration(jmsConfiguration);
        return activeMQComponent;
    }
}
