package by.bsu.cities.controller;

import by.bsu.cities.exception.entity.ErrorData;
import by.bsu.cities.exception.RestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorController {
    @ExceptionHandler({RestException.class})
    public ResponseEntity<ErrorData> handleException(RestException exception) {
        log.error("Error processing REST request with status code {} and message: {}",
                exception.getStatusCode(),
                exception.getMessage());
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new ErrorData(exception));
    }
}
