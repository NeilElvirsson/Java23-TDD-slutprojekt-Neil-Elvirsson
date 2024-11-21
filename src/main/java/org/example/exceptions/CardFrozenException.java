package org.example.exceptions;

public class CardFrozenException extends SecurityException {
    public CardFrozenException(String message) {
        super(message);
    }
}
