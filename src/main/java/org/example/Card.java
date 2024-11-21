package org.example;

public class Card {
    private final String cardID;
    private final String hashedPinCode;  // Förbättrad hantering för säkerhet
    private int failedAttempts;
    private boolean locked;

    public Card(String cardID, String pinCode) {
        this.cardID = cardID;
        this.hashedPinCode = hashPin(pinCode);
        this.locked = false;
        this.failedAttempts = 0;
    }

    private String hashPin(String pinCode) {
        return pinCode; 
    }

    public String getCardId() {

        return cardID;
    }

    public int getFailedAttempts() {

        return this.failedAttempts;

    }

    public void incrementFailedAttempts() {

        this.failedAttempts++;
    }


    public void resetFailedAttempts() {

        this.failedAttempts = 0;
    }


}
