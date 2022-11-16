package uz.atm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import uz.atm.dto.etp.EtpRequestDto;
import uz.atm.dto.etp.EtpResultDto;
import uz.atm.dto.justice.JusticeError;
import uz.atm.dto.justice.JusticeRequestDto;
import uz.atm.entity.Result;
import uz.atm.exception.BadRequestException;
import uz.atm.exception.JsonParserException;
import uz.atm.exception.ServiceUnavailableException;
import uz.atm.exception.messages.ApiMessages;
import uz.atm.properties.JusticeRequestProperties;
import uz.atm.service.caller.JusticeCaller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class JusticeService {

    private final ObjectMapper objectMapper;
    private final JusticeCaller justiceCaller;
    private final ResultService resultService;
    private final RabbitMqService rabbitMqService;
    private final JusticeRequestProperties justiceRequestProperties;


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
                                            new JusticeRequestDto.Params(basePinfl, listAtomicReference.get())), "/")
                            )
                            .mapNotNull(f -> {
                                if (f.result == null) {
                                    JusticeError error = f.error;
                                    Integer code = error.code;
                                    if (code == -32001) {
                                        log.error("Error : {}, Code : {}, Message : {}", ApiMessages.ERROR_WHILE_CALLING_JUSTICE, code, error.message);
                                        throw new ServiceUnavailableException(error.message);
                                    } else if (code == -32011) {
                                        log.warn("Error : Bad request sent to service, Code : {}, Message : {}", code, error.message);
                                        throw new BadRequestException(error.message);
                                    } else if (code == -32010) {
                                        log.warn("Error : Bad request sent to service, Code : {}, Message : {}", code, error.message);
                                        throw new BadRequestException(error.message);
                                    } else if (code == -32004) {
                                        log.error("Error : {}, Code : {}, Message : {}", ApiMessages.ERROR_WHILE_CALLING_JUSTICE, code, error.message);
                                        throw new ServiceUnavailableException(error.message);
                                    } else {
                                        log.error("Error : {},Code : {},Message : {}", ApiMessages.ERROR_WHILE_CALLING_JUSTICE, code, error.message);
                                        throw new ServiceUnavailableException(error.message);
                                    }
                                } else
                                    return f.result;
                            })
                            .publishOn(Schedulers.boundedElastic())
                            .flatMap(f -> {
                                f.forEach((K, V) -> {
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
                    rabbitMqService.sendResult(result, etpRequestDto.etpId);
                    return Mono.just(result);
                });
    }

    public void consume(String fileBody) {
        try {
            EtpRequestDto etpRequestDto = objectMapper.readValue(fileBody, EtpRequestDto.class);
            this.sendJustice(etpRequestDto).subscribe();
        } catch (JsonProcessingException e) {
            throw new JsonParserException(ExceptionUtils.getRootCauseMessage(e));
        }
    }
//
//    public Mono<List<EtpResultDto>> sendJusticeJony(EtpRequestDto etpRequestDto) {
//        List<String> base = etpRequestDto.base;
//        List<String> check = etpRequestDto.check;
//        List<EtpResultDto> result = new ArrayList<>();
//        return Flux.fromIterable(base)
//                .flatMap(basePinfl -> {
//                    ArrayList<EtpResultDto> tempList = new ArrayList<>();
//                    return justiceCaller
//                            .postCall(new JusticeRequestDto(",", ",", ",", new JusticeRequestDto.Params(basePinfl, check)), "qatgadr")
//                            .flatMap(f -> {
//                                f.result.forEach((K, V) -> {
//                                    tempList.add(new EtpResultDto(etpRequestDto.requestId, basePinfl, K, V));
//                                });
//                                return Mono.just(tempList);
//                            });
//                }).flatMap(etpResultDtos -> {
//                    result.addAll(etpResultDtos);
//                    return Mono.just(etpResultDtos);
//                }).collectList()
//                .flatMap(a -> Mono.just(result));
//
//    }

}