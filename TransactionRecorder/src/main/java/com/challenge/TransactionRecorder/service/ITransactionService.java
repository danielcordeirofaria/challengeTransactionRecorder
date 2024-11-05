package com.challenge.TransactionRecorder.service;

import com.challenge.TransactionRecorder.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public interface ITransactionService {

    String currencyConversion(int idTransaction, String country, String currency);

    ResponseEntity<?> saveTransaction(Transaction transaction);

    ArrayList<Transaction> getAllTransactions();
}
