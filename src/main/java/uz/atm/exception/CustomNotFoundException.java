package uz.atm.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 1:18 PM
 **/
public class CustomNotFoundException extends NestedRuntimeException {
    public CustomNotFoundException(String msg) {
        super(msg);
    }
}
