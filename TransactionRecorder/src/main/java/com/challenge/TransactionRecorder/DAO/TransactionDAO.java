package com.challenge.TransactionRecorder.DAO;

import com.challenge.TransactionRecorder.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDAO extends CrudRepository<Transaction, Integer> {

    Transaction getByIdTransaction(int idTransaction);

}
