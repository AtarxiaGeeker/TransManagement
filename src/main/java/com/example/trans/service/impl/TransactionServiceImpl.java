package com.example.trans.service.impl;

import com.example.trans.model.Transaction;
import com.example.trans.repository.MemoryTransactionRepository;
import com.example.trans.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private MemoryTransactionRepository repository;

    @Override
    public Transaction create(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public Transaction update(String id, Transaction transaction) {
        return repository.update(id, transaction);
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Transaction findById(String id) {
        return repository.findById(id);
    }
}
