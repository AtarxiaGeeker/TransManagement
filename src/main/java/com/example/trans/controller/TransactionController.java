package com.example.trans.controller;

import com.example.trans.common.ApiResponse;
import com.example.trans.model.Transaction;
import com.example.trans.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * 创建交易
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@RequestBody @Valid Transaction transaction) {
        Transaction created = transactionService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created));
    }

    /**
     * 修改交易
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> updateTransaction(@PathVariable String id,
                                                         @RequestBody @Valid Transaction updatedTransaction) {
        Transaction result = transactionService.update(id, updatedTransaction);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(result));
    }

    /**
     * 删除交易
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body((ApiResponse<Void>) ApiResponse.success());
    }

    /**
     * 分页查询
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Transaction>>> getTransactions(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> pageResult = transactionService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(pageResult));
    }

    /**
     * 根据交易ID查询交易
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable String id) {
        Transaction result = transactionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(result));
    }

}
