package uz.atm.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.atm.dto.dto.ResultDto;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.service.caller.JusticeCaller;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Service
public class JusticeService {

    private final JusticeCaller justiceCaller;
    private final ResultService resultService;

    public JusticeService(JusticeCaller justiceCaller, ResultService resultService) {
        this.justiceCaller = justiceCaller;
        this.resultService = resultService;
    }


    public Mono<Object> sendJustice(EtpRequestDto etpRequestDto) {
        AtomicReference<List<EtpResultDto>> resultats = new AtomicReference<>();

        Mono.just(etpRequestDto.base)
                .flatMap(fl -> {
                    fl.forEach(
                            f -> {
                                for (String s : etpRequestDto.check) {
                                    resultService.checkPinfls(f, s)
                                            .doOnNext(resultDto -> {
                                                if (resultDto.status)
                                                    resultats.get().add(new EtpResultDto(etpRequestDto.requestId, resultDto.base, resultDto.check, resultDto.result));
                                            });
                                }

                            });
                })
    }

}
