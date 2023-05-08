package com.sebastianabril.pos.api.exceptions;

public class NegativeOrZeroQuantityException extends RuntimeException {

    public NegativeOrZeroQuantityException(String message) {
        super(message);
    }
}
