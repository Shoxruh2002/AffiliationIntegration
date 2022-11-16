package uz.atm.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.properties.RabbitMQProperties;

import java.util.List;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/21/22 2:38 PM
 **/
@Service
public class RabbitMqService {


    private final RabbitTemplate rabbitTemplate;
    private final JusticeService justiceService;
    private final RabbitMQProperties rabbitMQProperties;

    public RabbitMqService(RabbitTemplate rabbitTemplate, JusticeService justiceService, RabbitMQProperties rabbitMQProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.justiceService = justiceService;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    public void send(Object message) {
        rabbitTemplate.convertAndSend("common", "push.*", message);
    }

    public void sendResult(List<EtpResultDto> message,Integer etpId) {
        new Thread(() -> {
            message
                    .forEach(f -> rabbitTemplate.convertAndSend("common", "push.*", f)
                    );
        }).start();
    }

    @RabbitListener(queues = {"${etp.rabbit.consumer-queue-name}"})
    public void listener(String str) {
        justiceService.consume(str);
    }
}
