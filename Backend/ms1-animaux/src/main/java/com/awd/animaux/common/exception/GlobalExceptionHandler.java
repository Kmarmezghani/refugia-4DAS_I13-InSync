package com.awd.animaux.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/** Même corps JSON d'erreur dans tous les services : timestamp, status, error, message, path (section 8.2). */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
    }

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<ErrorResponse> introuvable(RessourceIntrouvableException e, HttpServletRequest req) {
        return reponse(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    @ExceptionHandler(ConflitMetierException.class)
    public ResponseEntity<ErrorResponse> conflit(ConflitMetierException e, HttpServletRequest req) {
        return reponse(HttpStatus.CONFLICT, e.getMessage(), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalide(MethodArgumentNotValidException e, HttpServletRequest req) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(" ; "));
        return reponse(HttpStatus.BAD_REQUEST, message, req);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> illisible(HttpMessageNotReadableException e, HttpServletRequest req) {
        return reponse(HttpStatus.BAD_REQUEST, "Corps de requête illisible ou valeur inconnue (vérifier les énumérations)", req);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> typeIncorrect(MethodArgumentTypeMismatchException e, HttpServletRequest req) {
        return reponse(HttpStatus.BAD_REQUEST, "Valeur incorrecte pour le paramètre " + e.getName(), req);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> autre(Exception e, HttpServletRequest req) {
        // Erreurs standard de Spring MVC (route inconnue 404, méthode non permise 405...) : on garde leur code.
        if (e instanceof org.springframework.web.ErrorResponse er) {
            HttpStatus status = HttpStatus.valueOf(er.getStatusCode().value());
            return reponse(status, status.getReasonPhrase(), req);
        }
        log.error("Erreur inattendue sur {}", req.getRequestURI(), e);
        return reponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne", req);
    }

    private static ResponseEntity<ErrorResponse> reponse(HttpStatus status, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, req.getRequestURI()));
    }
}
