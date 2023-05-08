package com.sebastianabril.pos.api.exceptions;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
