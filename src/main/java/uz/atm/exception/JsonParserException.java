package uz.atm.exception;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 11/16/22 11:55 AM
 **/
public class JsonParserException extends RuntimeException {
    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException() {
    }
}
