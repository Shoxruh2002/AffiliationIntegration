package uz.atm.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import uz.atm.dto.AppErrorDto;
import uz.atm.exception.BadRequestException;
import uz.atm.exception.JsonParserException;
import uz.atm.exception.ServiceUnavailableException;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 11/16/22 10:24 AM
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            ServiceUnavailableException.class,
            NullPointerException.class,
            JsonParserException.class
    })
    public Mono<AppErrorDto> intervalServerErrorHandler(RuntimeException ex) {
        AppErrorDto appErrorDto = new AppErrorDto(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.just(appErrorDto);
    }

    @ExceptionHandler(value = {
            BadRequestException.class
    })
    public Mono<AppErrorDto> badRequestHandler(RuntimeException ex) {
        AppErrorDto appErrorDto = new AppErrorDto(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return Mono.just(appErrorDto);
    }
}
