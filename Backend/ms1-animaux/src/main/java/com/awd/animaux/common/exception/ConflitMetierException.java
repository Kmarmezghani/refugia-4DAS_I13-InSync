package com.awd.animaux.common.exception;

/** Règle métier non respectée (transition interdite, trop de photos...) : traduite en 409. */
public class ConflitMetierException extends RuntimeException {

    public ConflitMetierException(String message) {
        super(message);
    }
}
