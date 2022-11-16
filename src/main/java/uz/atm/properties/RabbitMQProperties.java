package uz.atm.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 11/16/22 11:39 AM
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "etp.rabbit")
public class RabbitMQProperties {

    private String consumerQueueName;

    private String publisherExchangeName;

    private String publisherRoutingKey;
}
