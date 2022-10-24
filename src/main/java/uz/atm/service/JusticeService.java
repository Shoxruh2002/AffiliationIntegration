package uz.atm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.dto.justice.JusticeRequestDto;
import uz.atm.entity.Result;
import uz.atm.properties.JusticeAPiProperties;
import uz.atm.properties.JusticeRequestProperties;
import uz.atm.service.caller.JusticeCaller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Service
@RequiredArgsConstructor
public class JusticeService {

    private final JusticeCaller justiceCaller;
    private final ResultService resultService;
    private final RabbitMqService rabbitMqService;
    private final JusticeRequestProperties justiceRequestProperties;
    private final JusticeAPiProperties justiceAPiProperties;


    public Mono<List<EtpResultDto>> sendJustice(EtpRequestDto etpRequestDto) {
        List<String> base = etpRequestDto.base;
        List<String> check = etpRequestDto.check;
        List<EtpResultDto> result = new ArrayList<>();
        return Flux.fromIterable(base)
                .flatMap(basePinfl -> {
                    AtomicReference<List<String>> listAtomicReference = new AtomicReference<>(check);
                    List<EtpResultDto> tempList = new ArrayList<>();
                    return resultService
                            .checkPinfls(basePinfl, check)
                            .doOnNext(f ->
                                    f.forEach(
                                            checkedPinfl -> {
                                                listAtomicReference.get().remove(checkedPinfl.getCheckPinfl());
                                                tempList.add(new EtpResultDto(etpRequestDto.requestId, basePinfl, checkedPinfl.getCheckPinfl(), checkedPinfl.getResult()));
                                            }
                                    ))
                            .flatMap(m -> justiceCaller
                                    .postCall(new JusticeRequestDto(
                                            justiceRequestProperties.getJsonRpc(), justiceRequestProperties.getId(), justiceRequestProperties.getMethod(),
                                            new JusticeRequestDto.Params(basePinfl, listAtomicReference.get())), justiceAPiProperties.getBaseUrl() + justiceAPiProperties.getApi().getCheckApi())
                            )
                            .publishOn(Schedulers.boundedElastic())
                            .flatMap(f -> {
                                f.result.forEach((K, V) -> {
                                    resultService.save(new Result(etpRequestDto.requestId.toString(), basePinfl, K, V));
                                    tempList.add(new EtpResultDto(etpRequestDto.requestId, basePinfl, K, V));
                                });
                                return Mono.just(tempList);
                            });
                })
                .publishOn(Schedulers.boundedElastic())
                .flatMap(etpResultDtos -> {
                    result.addAll(etpResultDtos);
                    return Mono.just(etpResultDtos);
                }).collectList()
                .flatMap(a -> {
                    rabbitMqService.sendResult(result);
                    return Mono.just(result);
                });

    }

    public Mono<List<EtpResultDto>> sendJusticeJony(EtpRequestDto etpRequestDto) {
        List<String> base = etpRequestDto.base;
        List<String> check = etpRequestDto.check;
        List<EtpResultDto> result = new ArrayList<>();
        return Flux.fromIterable(base)
                .flatMap(basePinfl -> {
                    ArrayList<EtpResultDto> tempList = new ArrayList<>();
                    return justiceCaller
                            .postCall(new JusticeRequestDto(",", ",", ",", new JusticeRequestDto.Params(basePinfl, check)), "qatgadr")
                            .flatMap(f -> {
                                f.result.forEach((K, V) -> {
                                    tempList.add(new EtpResultDto(etpRequestDto.requestId, basePinfl, K, V));
                                });
                                return Mono.just(tempList);
                            });
                }).flatMap(etpResultDtos -> {
                    result.addAll(etpResultDtos);
                    return Mono.just(etpResultDtos);
                }).collectList()
                .flatMap(a -> Mono.just(result));

    }
    //
//
//    public Flux<EtpResultDto> sendJusticenew(EtpRequestDto etpRequestDto) {
//        AtomicReference<List<EtpResultDto>> resultats = new AtomicReference<>(new ArrayList<>());
//        AtomicReference<List<String>> checks = new AtomicReference<>(etpRequestDto.check);
//        AtomicReference<String> basePinfl = new AtomicReference<>();
//        return Flux.fromIterable(etpRequestDto.base)
//                .flatMap(basePin -> {
//                    basePinfl.set(basePin);
//                    return this.sortList(basePin, checks.get());
//                })
//                .publishOn(Schedulers.boundedElastic())
//                .doOnNext(d -> resultats.get()
//                        .add(
//                                new EtpResultDto(etpRequestDto.requestId, d.getBasePinfl(), d.getCheckPinfl(), d.getResult())))
//                .map(Result::getCheckPinfl)
//                .doOnNext(f -> checks.get().remove(f))
//                .flatMap(f -> Mono.just(basePinfl.get()))
//                .flatMap(f ->
//                        justiceCaller
//                                .postCall(new JusticeRequestDto(",", ",", ",", new JusticeRequestDto.Params(f, checks.get())), "qatgadr"))
//                .publishOn(Schedulers.boundedElastic())
//                .map(f -> {
//                    f.result.forEach((K, V) -> {
//                        resultats.get().add(new EtpResultDto(etpRequestDto.requestId, basePinfl.get(), K, V));
//                        resultService.save(new Result(etpRequestDto.requestId.toString(), basePinfl.get(), K, V));
//                    });
//                    return resultats.get();
//                })
//                .flatMap(Flux::fromIterable);
//
//    }
}