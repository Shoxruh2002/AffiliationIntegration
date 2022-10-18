package uz.atm.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import uz.atm.dto.dto.ResultDto;
import uz.atm.repository.ResultRepository;

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

    public Mono<ResultDto> checkPinfls(String f, String s) {
        return resultRepository.findByBasePinflAndCheckPinfl(f, s)
                .map(m -> new ResultDto(m.getBasePinfl(), m.getCheckPinfl(), m.getResult()))
                .switchIfEmpty(Mono.just(new ResultDto(false)));
    }
}
