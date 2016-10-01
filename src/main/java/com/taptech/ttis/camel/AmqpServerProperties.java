package com.taptech.ttis.camel;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by tap on 5/7/16.
 */
@ConfigurationProperties(prefix = "amqp", ignoreUnknownFields = true)
public class AmqpServerProperties extends BaseServerProperties{
    /*
    # AMQP related
agora.amqp.host=localhost
agora.amqp.port=5672
agora.amqp.username=guest
agora.amqp.password=guest
agora.amqp.virtual-host=/
     */
    private static final String SIXTY = "60";

    private String virtualHost;
    private String serverProtocolName;
    private String username;
    private Integer concurrentConsumers;
    private String heartbeat = SIXTY;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServerProtocolName() {
        return serverProtocolName;
    }

    public void setServerProtocolName(String serverProtocolName) {
        this.serverProtocolName = serverProtocolName;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public Integer getConcurrentConsumers() {
        return (null == concurrentConsumers)?1:concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    // rabbitmq://127.0.0.1:5672/notification.event.exchange?routingKey=smtp.event
    public String serverURI(){
        StringBuilder sb = new StringBuilder(serverProtocolName);
        sb.append("://");
        sb.append(host).append(":").append(port);
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AmqpServerProperties{");
        sb.append("concurrentConsumers=").append(concurrentConsumers);
        sb.append(", virtualHost='").append(virtualHost).append('\'');
        sb.append(", serverProtocolName='").append(serverProtocolName).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", heartbeat='").append(heartbeat).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
