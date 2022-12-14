package uz.atm.service.caller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import uz.atm.dto.justice.JusticeRequestDto;
import uz.atm.dto.justice.JusticeResponse;
import uz.atm.exception.messages.ApiMessages;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 11:01 AM
 **/


@Slf4j
@Service
public class JusticeCaller {
    private final WebClient webClient;

    public JusticeCaller(@Qualifier("justice-web-client") WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<JusticeResponse> postCall(JusticeRequestDto request, String endpoint) {
//        Map<String, Boolean> map = new HashMap<>();
//        for (String s : request.params.toCheck) {
//            map.put(s, true);
//            System.out.println(s);
//        }
//        System.out.println(request.params.basePin);
//        return Mono.just(new JusticeResponse("2.0", "123", new JusticeError(-32323, "Exceptionni biri")));
        log.info("postCall is calling with request : {},  endpoint : {}", request.toString(), endpoint);
        return webClient.post()
                .uri(endpoint)
                .bodyValue(request)
                .exchangeToMono(
                        result -> {
                            if (result.statusCode() != HttpStatus.OK)
                                log.error("{} endpoint : [{}], statusCode: [{}]", ApiMessages.ERROR_WHILE_CALLING_JUSTICE, endpoint, result.statusCode());
                            log.info("Log prefix : {}", result.logPrefix());
                            log.info("Response to String : {}", result.toString());
                            return result.bodyToMono(new ParameterizedTypeReference<>() {
                            });
                        }
                );
    }

}
