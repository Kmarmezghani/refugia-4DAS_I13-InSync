package com.awd.dons.common.exception;

/** Paramètres incohérents (ex. date de début après date de fin) → 400. */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
