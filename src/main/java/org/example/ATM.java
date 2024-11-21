package org.example;  // Paketdeklaration

import org.example.Bank;
import org.example.Card;
import org.example.exceptions.InsufficientFundsException;


public class ATM {
    private final Bank bankService;
    private Card activeCard;

    public ATM(Bank bankService) {
        this.bankService = bankService;
    }

    public Card getCard() {
         return this.activeCard;
    }
    private void ensureCardInserted() {
        if (activeCard == null) {
            throw new IllegalStateException("Inget kort är insatt.");
        }
    }

    private void ensureCardIsNotFrozen() {
        if (bankService.isCardFrozen(activeCard.getCardId())) {
            throw new SecurityException("Kortet är låst och kan inte användas.");
        }
    }

    public void insertCard(Card card) {

        if(card == null) {
            throw new IllegalArgumentException("Kortet får inte vara null.");
        }

        if (bankService.isCardFrozen(card.getCardId())) {
            throw new SecurityException("Kortet är låst och kan inte användas.");
        }

        this.activeCard = card;
    }

    public boolean authenticatePin(String pin) {
        ensureCardInserted();
        ensureCardIsNotFrozen();

        if (bankService.validatePin(activeCard.getCardId(), pin)) {
            activeCard.resetFailedAttempts();
            return true;
        } else {
            handleFailedPinAttempt();
            return false;
        }
    }

    private void handleFailedPinAttempt() {
        activeCard.incrementFailedAttempts();

        bankService.recordFailedAttempt(activeCard.getCardId());

        int attemptsRemaining = 3 - activeCard.getFailedAttempts();
       // System.out.println("Antal försök kvar" + attemptsRemaining);

        if (attemptsRemaining <= 0) {
            System.out.println("Låser kortet!");
            bankService.lockCard(activeCard.getCardId());
            throw new SecurityException("Kortet är nu låst efter tre felaktiga försök.");


        } else {
            System.out.println("Fel pinkod. Försök kvar: " + attemptsRemaining);

        }
    }

    public double checkBalance() {
        ensureCardInserted();
        ensureCardIsNotFrozen();
        return bankService.getBalance(activeCard.getCardId());

    }

    public void withdrawAmount(double amount) throws InsufficientFundsException {
        ensureCardInserted();
        ensureCardIsNotFrozen();

        if (amount < 20) {
            throw new IllegalArgumentException("Minsta uttagsbelopp är 20 kronor.");
        }

        double currentBalance = bankService.getBalance(activeCard.getCardId());
        if (currentBalance >= amount) {
            bankService.withdraw(activeCard.getCardId(), amount);
        } else {
            throw new InsufficientFundsException("Otillräckligt saldo för uttag.");
        }
    }

    public void endSession() {
        activeCard = null;
        System.out.println("Session avslutad. Vänligen ta ditt kort.");
    }
}

