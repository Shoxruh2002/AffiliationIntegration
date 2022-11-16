package uz.atm.exception;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 11/16/22 10:29 AM
 **/
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
