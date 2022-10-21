package uz.atm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.service.JusticeService;
import uz.atm.service.RabbitMqService;

import java.util.List;

/**
 * Author: Bekpulatov Shoxruh
 * Date: 19/10/22
 * Time: 10:10
 */
@RestController
@RequestMapping("/justice")
public class JusticeController {

    private final JusticeService justiceService;
    private final RabbitMqService rabbitMqService;


    public JusticeController(JusticeService justiceService, RabbitMqService rabbitMqService) {
        this.justiceService = justiceService;
        this.rabbitMqService = rabbitMqService;
    }


    @PostMapping("/check")
    public Mono<List<EtpResultDto>> check(@RequestBody EtpRequestDto dto) {
        return justiceService.sendJustice(dto);
    }

    @PostMapping("/sendRabbit")
    public Mono<String> send(@RequestBody String dto) {
        return Mono.fromCallable(() -> {
            rabbitMqService.send(dto);
            return "Okey😀";
        });
    }
}
