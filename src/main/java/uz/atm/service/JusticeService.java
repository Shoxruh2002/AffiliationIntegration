package uz.atm.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.dto.justice.JusticeRequestDto;
import uz.atm.entity.Result;
import uz.atm.service.caller.JusticeCaller;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public Mono<List<Result>> sortList(String basepin, List<String> checkList) {
        return resultService.checkPinfls(basepin, checkList)
                .onErrorReturn(new ArrayList<>());
    }


    public Flux<EtpResultDto> sendJustice(EtpRequestDto etpRequestDto) {
        AtomicReference<List<EtpResultDto>> resultats = new AtomicReference<>(new ArrayList<>());
        AtomicReference<List<String>> checks = new AtomicReference<>(etpRequestDto.check);
        return Flux.fromIterable(etpRequestDto.base)
                .flatMap(basePin -> {
                            AtomicReference<List<EtpResultDto>> resultList = new AtomicReference<>(new ArrayList<>());

                            return Mono.just(basePin)
                                    .flatMap(f -> this.sortList(basePin, checks.get()))
                                    .publishOn(Schedulers.boundedElastic())
                                    .doOnNext(d -> resultList.get()
                                            .addAll(d.stream()
                                                    .map(m -> new EtpResultDto(etpRequestDto.requestId, m.getBasePinfl(), m.getCheckPinfl(), m.getResult())).toList())
                                    )
                                    .map(m -> m.stream().map(Result::getCheckPinfl).toList())
                                    .doOnNext(f -> checks.get().removeAll(f))
                                    .then(Mono.just(basePin))
                                    .flatMap(f ->
                                            justiceCaller
                                                    .postCall(new JusticeRequestDto(",", ",", ",", new JusticeRequestDto.Params(f, checks.get())), "qatgadr"))
                                    .publishOn(Schedulers.boundedElastic())
                                    .map(f -> {
                                        AtomicReference<List<EtpResultDto>> etpResultDtos = new AtomicReference<>(new LinkedList<>());
                                        f.result.forEach((K, V) -> {
                                            etpResultDtos.get().add(new EtpResultDto(etpRequestDto.requestId, basePin, K, V));
                                        });
                                        return etpResultDtos.get();
                                    })
                                    .map(d -> {
//                                        d.forEach(item -> resultList.get().add(item));
                                        resultList.get().addAll(d);
                                        return resultList.get();
                                    });

                        }
                ).flatMap(m -> {
                    resultats.get().addAll(m);
                    return Flux.fromIterable(resultats.get());
                });
    }
}