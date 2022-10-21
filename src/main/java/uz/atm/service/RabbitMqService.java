package uz.atm.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.entity.Result;

import java.util.List;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/21/22 2:38 PM
 **/
@Service
public class RabbitMqService {


    private final RabbitTemplate rabbitTemplate;

    public RabbitMqService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object message) {
        rabbitTemplate.convertAndSend("common", "push.*", message);
    }

    public void sendResult(List<EtpResultDto> message) {
        new Thread(() -> {
            message
                    .forEach(f -> rabbitTemplate.convertAndSend("common", "push.*", f)
                    );
        }).start();
    }

    @RabbitListener(queues = {"cportal_ant_in"})
    public void listener(String str) {
        System.out.println(str);
    }
}
