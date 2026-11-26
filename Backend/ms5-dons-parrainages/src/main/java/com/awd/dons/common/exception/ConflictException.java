package com.awd.dons.common.exception;

/** Conflit avec une règle métier (doublon, transition interdite...) → 409. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
