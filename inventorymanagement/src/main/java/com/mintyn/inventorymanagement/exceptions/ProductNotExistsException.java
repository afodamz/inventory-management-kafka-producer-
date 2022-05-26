package com.mintyn.inventorymanagement.exceptions;

public class ProductNotExistsException extends RuntimeException {

    public ProductNotExistsException(String message) {
        super(message);
    }
}
