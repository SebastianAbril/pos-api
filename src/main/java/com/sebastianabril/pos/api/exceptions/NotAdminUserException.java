package com.sebastianabril.pos.api.exceptions;

public class NotAdminUserException extends RuntimeException {

    public NotAdminUserException(String message) {
        super(message);
    }
}
