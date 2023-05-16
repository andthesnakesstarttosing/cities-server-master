package by.bsu.cities.exception.entity;

import by.bsu.cities.exception.RestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorData {
    private final HttpStatus statusCode;
    private final String statusMessage;
    private final String message;

    public ErrorData(RestException exception) {
        this.statusCode = exception.getStatusCode();
        this.statusMessage = exception.getStatusMessage();
        this.message = exception.getMessage();
    }
}
