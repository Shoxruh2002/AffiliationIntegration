package uz.atm.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.atm.exception.messages.ApiMessages;

import javax.naming.ServiceUnavailableException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorWrapper.class);

    public static Throwable databaseErrorWrapper(Throwable err, String method) {
        LOG.error("Error ({}) : [{}]", method, err.getMessage());
        throw new DatabaseException(ApiMessages.INTERNAL_SERVER_ERROR + err.getMessage());
    }

    public static <T> Throwable serviceUnavailableWrapper(String err, String method, T response) throws ServiceUnavailableException {
        LOG.error("error response from [{}] {}", method, response);
        throw new ServiceUnavailableException(ApiMessages.SERVICE_UNAVAILABLE + err);
    }

}
