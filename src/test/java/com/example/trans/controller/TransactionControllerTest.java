package com.example.trans.controller;

import com.example.trans.model.Transaction;
import com.example.trans.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;


    @Test
    void test_createTransaction_ValidInput_Returns201() throws Exception {
        Transaction transaction = new Transaction("txn-001", "DEPOSIT", new BigDecimal(100), LocalDateTime.now());
        Mockito.when(transactionService.create(Mockito.any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "id": "txn-001",
                    "amount": 100.0,
                    "type": "DEPOSIT",
                    "dateTime": "2024-06-01T00:00:00Z"
                }
                """))
                .andExpect(status().isCreated());
    }

    @Test
    void test_updateTransaction_ValidId_Returns200() throws Exception {
        Transaction updated = new Transaction("txn-001", "DEPOSIT", new BigDecimal(200), LocalDateTime.now());
        Mockito.when(transactionService.update("txn-001", updated)).thenReturn(updated);

        mockMvc.perform(put("/api/transactions/txn-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "amount": 200.0,
                    "timestamp": "2024-06-01T12:00:00Z"
                }
                """))
                .andExpect(status().isOk());
    }

    @Test
    void test_updateTransaction_InvalidId_Returns404() throws Exception {
        Mockito.when(transactionService.update(Mockito.eq("invalid-id"), Mockito.any()))
                .thenThrow(new NoSuchElementException("Transaction not found"));

        mockMvc.perform(put("/api/transactions/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getTransactionById_Exists_Returns200() throws Exception {
        Transaction transaction = new Transaction("txn-001", "DEPOSIT", new BigDecimal(200), LocalDateTime.now());
        Mockito.when(transactionService.findById("txn-001")).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/txn-001"))
                .andExpect(status().isOk());
    }

    @Test
    void test_getTransactionById_NotExists_Returns404() throws Exception {
        Mockito.when(transactionService.findById("invalid-id"))
                .thenThrow(new NoSuchElementException("Not found"));

        mockMvc.perform(get("/api/transactions/invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_deleteTransaction_ValidId_Returns204() throws Exception {
        mockMvc.perform(delete("/api/transactions/txn-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_deleteTransaction_InvalidId_Returns404() throws Exception {
        Mockito.doThrow(new NoSuchElementException("Not found"))
                .when(transactionService).delete("invalid-id");

        mockMvc.perform(delete("/api/transactions/invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test_getTransactions_ReturnsPagedData() throws Exception {
        Page<Transaction> page = new PageImpl<>(
                Collections.singletonList(new Transaction("txn-001", "DEPOSIT", new BigDecimal(200), LocalDateTime.now())),
                PageRequest.of(0, 10),
                1
        );
        Mockito.when(transactionService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/transactions?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value("txn-001"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }
}
