package uz.atm.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.atm.entity.Result;
import uz.atm.exception.CustomNotFoundException;
import uz.atm.exception.messages.ApiMessages;
import uz.atm.repository.ResultRepository;

import java.util.List;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:50 AM
 **/
@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public Mono<Result> checkPinfls(String f, String s) {
        return resultRepository.findByBasePinflAndCheckPinfl(f, s)
                .switchIfEmpty(Mono.error(new CustomNotFoundException(ApiMessages.NOT_FOUND)));
    }

    public Mono<List<Result>> checkPinfls(String f, List<String> s) {
        return resultRepository.findAllByBasePinflAndCheckPinflIsIn(f, s).collectList()
                .switchIfEmpty(Mono.error(new CustomNotFoundException(ApiMessages.NOT_FOUND)));
    }


    public void save(Result result) {
        resultRepository.save(result).then();
    }
}
