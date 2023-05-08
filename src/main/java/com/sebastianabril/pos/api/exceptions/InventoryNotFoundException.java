package com.sebastianabril.pos.api.exceptions;

import org.aspectj.weaver.ast.Not;

public class InventoryNotFoundException extends NotFoundException {

    public InventoryNotFoundException(String message) {
        super(message);
    }
}
