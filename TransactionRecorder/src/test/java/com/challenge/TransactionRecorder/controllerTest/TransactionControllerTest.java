package com.challenge.TransactionRecorder.controllerTest;

import com.challenge.TransactionRecorder.controller.TransactionController;
import com.challenge.TransactionRecorder.model.Transaction;
import com.challenge.TransactionRecorder.service.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;


import java.util.ArrayList;
import java.util.Date;


import static net.bytebuddy.matcher.ElementMatchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionServiceImpl transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    public static ResultMatcher customMatcher(String expectedValue) {
        return result -> jsonPath("$.message", is(expectedValue)); // Access the "message" field
    }

    @Test
    public void testAddTransactionSuccess() throws Exception {
        Transaction transaction = new Transaction(1, "Test Transaction", new Date(), 100.0);

        // Mock the service to return a successful response
        when(transactionService.saveTransaction(any(Transaction.class)))
                .thenReturn(ResponseEntity.ok().body("Transaction registered successfully"));

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                    .andExpect(status().isOk())
                    .andExpect(customMatcher("Transaction registered successfully"));
    }

    @Test
    public void testGetAllTransactionsSuccess() throws Exception {

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, "Transaction 1", new Date(), 100.0));
        transactions.add(new Transaction(2, "Transaction 2", new Date(), 200.0));

        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllTransactionsFailure() throws Exception {
        when(transactionService.getAllTransactions()).thenThrow(new RuntimeException("Failed to retrieve transactions"));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred."));
    }

    @Test
    public void testGetTransactionConvertingCurrencySuccess() throws Exception {
        int idTransaction = 1;
        String country = "US";
        String currency = "USD";
        String expectedJson = "{\"transactionId\": 1, \"amount\": 100.00, \"currency\": \"USD\"}"; // Substitua pelo JSON esperado

        when(transactionService.currencyConversion(idTransaction, country, currency))
                .thenReturn(expectedJson);

        mockMvc.perform(get("/transactions/{idTransaction}/{country}/{currency}", idTransaction, country, currency))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void testGetTransactionConvertingCurrencyFailure() throws Exception {
        int idTransaction = 1;
        String country = "US";
        String currency = "USD";

        when(transactionService.currencyConversion(idTransaction, country, currency))
                .thenReturn(null);

        mockMvc.perform(get("/transactions/{idTransaction}/{country}/{currency}", idTransaction, country, currency))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Transaction or current not found."));
    }

}