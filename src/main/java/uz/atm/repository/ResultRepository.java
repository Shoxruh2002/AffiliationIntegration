package uz.atm.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.atm.entity.Result;

import java.util.List;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/

@Repository
public interface ResultRepository extends BaseRepository<Result> {

    Mono<Result> findByBasePinflAndCheckPinfl(String basePinfl, String checkPinfl);

    Flux<Result> findAllByBasePinflAndCheckPinflIsIn(String basePinfl, List<String> checkPinfl);
}
