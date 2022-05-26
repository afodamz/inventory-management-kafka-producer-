package com.mintyn.inventorymanagement.exceptions;

public class NoItemsInOrderException extends RuntimeException {

    public NoItemsInOrderException(String message) {
        super(message);
    }
}