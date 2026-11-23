package com.awd.animaux.common.exception;

/** Ressource inexistante : traduite en 404 par le GlobalExceptionHandler. */
public class RessourceIntrouvableException extends RuntimeException {

    public RessourceIntrouvableException(String message) {
        super(message);
    }

    public static RessourceIntrouvableException animal(Long id) {
        return new RessourceIntrouvableException("Animal " + id + " introuvable");
    }
}
