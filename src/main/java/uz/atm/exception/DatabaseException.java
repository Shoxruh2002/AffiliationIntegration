package uz.atm.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Author: Shoxruh Bekpulatov
 * Time: 10/18/22 1:12 PM
 **/
public class DatabaseException extends NestedRuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
