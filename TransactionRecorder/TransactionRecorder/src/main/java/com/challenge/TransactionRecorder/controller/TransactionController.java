package com.challenge.TransactionRecorder.controller;

import com.challenge.TransactionRecorder.model.Transaction;
import com.challenge.TransactionRecorder.service.ITransactionService;
import com.challenge.TransactionRecorder.service.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
        try {
            ResponseEntity<?> response = transactionService.saveTransaction(transaction);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An error occurred while registering the new transaction."));
        }
    }

    @GetMapping
    public ResponseEntity<ArrayList<Transaction>> getAllTransactions() {
        ArrayList<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
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
