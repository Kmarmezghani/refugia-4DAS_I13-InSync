package com.awd.veterinaire.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body,
            HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        HttpStatus status = HttpStatus.valueOf(statusCode.value());
        String message = status == HttpStatus.BAD_REQUEST ? "Requête invalide." : status.getReasonPhrase();
        if (exception instanceof ResponseStatusException responseException) {
            message = responseException.getReason();
        }
        ErreurApi erreur = new ErreurApi(Instant.now(), status.value(), status.getReasonPhrase(), message, path);
        return super.handleExceptionInternal(exception, erreur, headers, statusCode, request);
    }

    public record ErreurApi(Instant timestamp, int status, String error, String message, String path) {
    }
}
