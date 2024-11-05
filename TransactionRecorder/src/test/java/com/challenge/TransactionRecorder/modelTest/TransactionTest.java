package com.challenge.TransactionRecorder.modelTest;

import com.challenge.TransactionRecorder.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void testValidTransaction() {
        Transaction transaction = new Transaction(1, "Compra de teste", new Date(), 100.50);
        assertNotNull(transaction);
        assertEquals(1, transaction.getIdTransaction());
        assertEquals("Compra de teste", transaction.getDescription());
        assertEquals(100.50, transaction.getPuchaseAmount());
    }

    @Test
    void testValidIdTransactionPositive() {
        assertDoesNotThrow(() -> {
            new Transaction(3, "Valid Transaction", new Date(), 10.0);
        });
        Transaction transaction = new Transaction(3, "Valid Transaction", new Date(), 10.0);
        assertEquals(3, transaction.getIdTransaction());
    }

    @Test
    void testInvalidIdTransactionEqualZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(0, "New transaction with an invalid idTransaction", new Date(), 50.0);
        });
    }

    @Test
    void testInvalidIdTransactionSmallerZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(-3, "New transaction with an invalid idTransaction", new Date(), 50.0);
        });
    }


    @Test
    void testInvalidTransactionDescriptionTooLong() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(2, "New transaction with a description that is longer than fifty characters", new Date(), 50.0);
        });
    }

    //negative teste transaction null
    @Test
    void testInvalidTransactionDescriptionNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(2, null, new Date(), 50.0);
        });
    }

    @Test
    void testInvalidTransactionAmountNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(3, "Invalid Transaction", new Date(), -10.0);
        });
    }

    @Test
    void testValidTransactionAmountPositive() {
        assertDoesNotThrow(() -> {
            new Transaction(3, "Valid Transaction", new Date(), 10.0);
        });
        Transaction transaction = new Transaction(3, "Valid Transaction", new Date(), 10.0);
        assertEquals(10.0, transaction.getPuchaseAmount());
    }

    @Test
    void testInvalidTransactionAmountZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(4, "Invalid Transaction", new Date(), 0.0);
        });
    }


}
