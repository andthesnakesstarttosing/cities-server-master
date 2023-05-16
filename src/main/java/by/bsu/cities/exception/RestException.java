package by.bsu.cities.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class RestException extends Exception {
    private final HttpStatus statusCode;
    private final String statusMessage;

    public RestException(String message, HttpStatus statusCode, String statusMessage) {
        super(message);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public RestException(String message, Throwable cause, HttpStatus statusCode, String statusMessage) {
        super(message, cause);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public RestException(Throwable cause, HttpStatus statusCode, String statusMessage) {
        super(cause);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public RestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus statusCode, String statusMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
