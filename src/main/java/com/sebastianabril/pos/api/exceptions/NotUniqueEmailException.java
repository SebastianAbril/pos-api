package com.sebastianabril.pos.api.exceptions;

public class NotUniqueEmailException extends RuntimeException {

    public NotUniqueEmailException(String message) {
        super(message);
    }
}
