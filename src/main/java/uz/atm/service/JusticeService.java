package uz.atm.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uz.atm.dto.dto.ResultDto;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.dto.justice.JusticeRequestDto;
import uz.atm.entity.Result;
import uz.atm.service.caller.JusticeCaller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    public Mono<List<Result>> sortList(String basepin, List<String> checkList) {
        AtomicReference<List<EtpResultDto>> list = new AtomicReference<>();
        return resultService.checkPinfls(basepin, checkList)
                .collectList();
    }


    public Mono<Object> sendJustice(EtpRequestDto etpRequestDto) {
        AtomicReference<List<EtpResultDto>> resultats = new AtomicReference<>();
        AtomicReference<Mono<String>> basePinfl = null;
        Flux.fromIterable(etpRequestDto.base).map(basePin -> {
                    basePinfl.set(Mono.just(basePin));
                    AtomicReference<List<EtpResultDto>> resultList = new AtomicReference<>();

                    return basePinfl.get()
                            .flatMap(f -> this.sortList(basePin, etpRequestDto.check))
                            .doOnNext(d -> resultList
                                    .set(d.stream()
                                            .map(m -> new EtpResultDto(etpRequestDto.requestId, m.getBasePinfl(), m.getCheckPinfl(), m.getResult())).toList())
                            )
                            .map(m -> m.stream().map(Result::getCheckPinfl).toList())
                            .doOnNext(f -> etpRequestDto.check.removeAll(f))
                            .then(basePinfl.get())
                            .flatMap(f ->
                                    justiceCaller
                                            .postCall(new JusticeRequestDto(",", ",", ",", new JusticeRequestDto.Params(f, etpRequestDto.check)), "qatgadr"))
                            .map(f -> {
                                List<EtpResultDto> etpResultDtos = new LinkedList<>();
                                f.result.forEach((K, V) -> {
                                    etpResultDtos.add(new EtpResultDto(etpRequestDto.requestId, basePin, K, V));
                                });
                                return etpResultDtos;
                            })
                            .publishOn(Schedulers.boundedElastic())
                            .map(d -> {
                                resultList.get().addAll(d);
                                return resultList.get();
                            });

                }
        ).doOnNext(d -> d resultats.get().addAll(d))
    }
}