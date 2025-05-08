package com.example.trans.service;

import com.example.trans.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Transaction create(Transaction transaction);
    void delete(String id);
    Transaction update(String id, Transaction transaction);
    Page<Transaction> findAll(Pageable pageable);
    Transaction findById(String id);
}
