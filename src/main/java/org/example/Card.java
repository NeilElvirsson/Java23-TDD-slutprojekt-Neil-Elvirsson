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
        // Placeholder för PIN-hashning. För riktiga system bör du använda en säker hash-algoritm.
        return pinCode; // För enkelhetens skull
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

    public void setFailedAttempts(int attempts) {

        this.failedAttempts = attempts;
    }

    public void resetFailedAttempts() {

        this.failedAttempts = 0;
    }

    public boolean isLocked() {

        return locked;
    }

    public void lockCard() {

        this.locked = true;
    }

    public boolean verifyPin(String pinCode) {

        return hashedPinCode.equals(hashPin(pinCode));
    }


}
