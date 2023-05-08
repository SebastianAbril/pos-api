package com.sebastianabril.pos.api.exceptions;

public class NotEnoughQuantityException extends RuntimeException {

    public NotEnoughQuantityException(String message) {
        super(message);
    }
}
