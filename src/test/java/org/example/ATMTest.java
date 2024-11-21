package org.example;

import org.example.exceptions.CardFrozenException;
import org.example.exceptions.InsufficientFundsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



class ATMTest {
    private Bank bankService;
    private ATM atm;
    private Card card;

    @BeforeEach
    void setUp() {
        // Mocka Bank och Card
        bankService = mock(Bank.class);
        card = mock(Card.class);

        // Skapa en ATM-instans
        atm = new ATM(bankService);

    }

    @DisplayName("Test for when card is frozen")
    @Test
    void testInsertCard_WhenCardIsFrozen_ThrowsSecurityException() {
        // Förberedelse: kortet är fryst
        when(bankService.isCardFrozen(anyString())).thenReturn(true);
        when(card.getCardId()).thenReturn("1234");

        // Testa att kortet inte kan sättas in
        assertThrows(SecurityException.class, () -> atm.insertCard(card));
    }

    @DisplayName("Test for when card is not frozen")
    @Test
    void testInsertCard_WhenCardIsNotFrozen_CardIsInserted() {
        // Förberedelse: kortet är inte fryst
        when(bankService.isCardFrozen(anyString())).thenReturn(false);
        atm.insertCard(card);

        // Verifiera att kortet har satts in korrekt
        assertNotNull(atm.getCard());
    }

    @DisplayName("Test for when pin is correct")
    @Test
    void testEnterPin_WhenPinIsCorrect_ReturnsTrue() {
        // Förberedelse: PIN är korrekt
        when(bankService.validatePin(anyString(), anyString())).thenReturn(true);
        when(card.getCardId()).thenReturn("1234");

        atm.insertCard(card);
        // Testa att PIN-verifieringen fungerar
        boolean result = atm.authenticatePin("1234");

        assertTrue(result);
    }

    @DisplayName("Test for when pin is incorrect")
    @Test
    void testEnterPin_WhenPinIsIncorrect_IncrementsFailedAttempts() {

        when(bankService.validatePin(anyString(), eq("wrongPin"))).thenReturn(false);

        doAnswer(invocation -> {
            // Simulera incrementering av försök
            int attempts = card.getFailedAttempts();
            when(card.getFailedAttempts()).thenReturn(attempts + 1);
            return null;
        }).when(card).incrementFailedAttempts();

        atm.insertCard(card);
        // Testa autentisering tre gånger
        atm.authenticatePin("wrongPin");
        atm.authenticatePin("wrongPin");
        assertThrows(SecurityException.class, () -> atm.authenticatePin("wrongPin"));

        // Verifiera att incrementFailedAttempts anropades tre gånger
        verify(card, times(3)).incrementFailedAttempts();

    }

    @DisplayName("Test for when balance is sufficient")
    @Test
    void testWithdraw_WhenBalanceIsSufficient_WithdrawsAmount() throws InsufficientFundsException {
        // Förberedelse: saldo är tillräckligt

        when(bankService.getBalance(anyString())).thenReturn(1000.0);
        when(bankService.isCardFrozen(anyString())).thenReturn(false);
        when(bankService.validatePin(anyString(), eq("1234"))).thenReturn(true);
        when(card.getCardId()).thenReturn("1234");

        atm.insertCard(card);
        atm.authenticatePin("1234");

        assertEquals(1000.0,atm.checkBalance(), "Saldo borde vara 1000");
        // Testa uttag
        atm.withdrawAmount(200);

        // Verifiera att bankens withdraw-metod anropades
        verify(bankService, times(1)).withdraw(anyString(), eq(200.0));
    }

    @DisplayName("Test for when balance is insufficient")
    @Test
    void testWithdraw_WhenBalanceIsInsufficient_ThrowsInsufficientFundsException() {
        // Förberedelse: saldo är otillräckligt
        when(bankService.getBalance(anyString())).thenReturn(100.0);
        when(bankService.isCardFrozen(anyString())).thenReturn(false);

        // Sätt in kortet och autentisera
        atm.insertCard(card);
        atm.authenticatePin("1234");

        // Testa att uttag misslyckas
        assertThrows(InsufficientFundsException.class, () -> atm.withdrawAmount(200));
    }

    @DisplayName("Test for end session")
    @Test
    void testEndSession() {
        // Förberedelse: Sätt in kortet och autentisera
        atm.insertCard(card);
        atm.authenticatePin("1234");

        // Testa att sessionen avslutas korrekt
        atm.endSession();
        assertNull(atm.getCard());  // Kontrollera att kortet har tagits ut
    }

}

