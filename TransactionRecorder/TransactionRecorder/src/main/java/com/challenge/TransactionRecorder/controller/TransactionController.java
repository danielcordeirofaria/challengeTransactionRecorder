package com.challenge.TransactionRecorder.controller;

import com.challenge.TransactionRecorder.model.Transaction;
import com.challenge.TransactionRecorder.service.TransactionServiceImpl;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionServiceImpl transactionService;

    private Logger logger;

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
        try {
            ResponseEntity<String> savedTransaction = transactionService.saveTransaction(transaction);
            return ResponseEntity.ok(savedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error saving transaction", e);
            return ResponseEntity.internalServerError().body("An error occurred while registering the new transaction.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        try {
            ArrayList<Transaction> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving transactions from the database.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{idTransaction}/{country}/{currency}")
    public ResponseEntity<?> getTransactionConvertingCurrency(@PathVariable("idTransaction") int idTransaction, @PathVariable("country") String country, @PathVariable("currency") String currency) {
        String transactionRes = transactionService.currencyConversion(idTransaction, country, currency);
        if(transactionRes != null) {
            return ResponseEntity.ok(transactionRes);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Transaction or current not found."));
        }
    }
}
