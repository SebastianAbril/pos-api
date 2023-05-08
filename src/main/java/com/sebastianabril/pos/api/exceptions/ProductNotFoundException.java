package com.sebastianabril.pos.api.exceptions;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
