package uz.atm.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.service.JusticeService;

/**
 * Author: Bekpulatov Shoxruh
 * Date: 19/10/22
 * Time: 10:10
 */
@RestController
@RequestMapping("/justice")
public class JusticeController {

    private final JusticeService justiceService;


    public JusticeController(JusticeService justiceService) {
        this.justiceService = justiceService;
    }


    @PostMapping("/check")
    public Flux<EtpResultDto> check(@RequestBody EtpRequestDto dto) {
        return justiceService.sendJustice(dto);
    }
}
