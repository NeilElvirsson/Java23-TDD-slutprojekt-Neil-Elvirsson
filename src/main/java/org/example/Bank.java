package org.example;

public interface Bank {
    boolean validatePin(String cardID, String pinCode);
    boolean isCardFrozen(String cardID);
    double getBalance(String cardID);
    void withdraw(String cardID, double amount);
    void lockCard(String cardID);
    void recordFailedAttempt(String cardID);

}
