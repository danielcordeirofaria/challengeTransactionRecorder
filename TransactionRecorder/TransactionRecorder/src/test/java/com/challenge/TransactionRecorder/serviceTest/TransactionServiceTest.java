package com.challenge.TransactionRecorder.serviceTest;

import com.challenge.TransactionRecorder.DAO.TransactionDAO;
import com.challenge.TransactionRecorder.model.Transaction;
import com.challenge.TransactionRecorder.service.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionDAO transactionDAO;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testValidateTransaction_descriptionTooLong() {
        Transaction transaction = new Transaction();
        transaction.setDescription("This is a very long description that exceeds the maximum allowed length.");

        assertThrows(IllegalArgumentException.class, () -> transactionService.validateTransaction(transaction));
    }

    @Test
    public void testValidateTransaction_existentId() {
        Transaction transaction = new Transaction();
        transaction.setIdTransaction(1);
        transaction.setDescription("Descrição da transação"); // Adicione esta linha
        when(transactionDAO.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> transactionService.validateTransaction(transaction));
    }

    @Test
    public void testValidateTransaction_amountLessThanOrEqualToZero() {
        Transaction transaction = new Transaction();
        transaction.setPuchaseAmount(0);
        transaction.setDescription("Descrição da transação");

        assertThrows(IllegalArgumentException.class, () -> transactionService.validateTransaction(transaction));
    }

    @Test
    public void testValidateTransaction_validTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDescription("Valid description");
        transaction.setIdTransaction(1);
        transaction.setPuchaseAmount(100);
        when(transactionDAO.existsById(1)).thenReturn(false);

        // Não deve lançar exceção
        transactionService.validateTransaction(transaction);
    }
}