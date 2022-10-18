package uz.atm.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 1:17 PM
 **/
public class ServiceUnavailableException extends NestedRuntimeException {
    public ServiceUnavailableException(String msg) {
        super(msg);
    }
}
