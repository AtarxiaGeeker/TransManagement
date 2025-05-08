package com.example.trans.exception;

public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String id) {
        super("Transaction already exists with ID: " + id);
    }
}
