package org.example.exceptions;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
/*
    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientFundsException() {
        super("Otillräckligt saldo för att genomföra transaktionen.");
    }

 */
}
