package com.example.trans.repository;

import com.example.trans.exception.DuplicateTransactionException;
import com.example.trans.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@Repository
public class MemoryTransactionRepository {
    // 存储交易信息
    private final Map<String, Transaction> storage = new ConcurrentHashMap<>();
    // 预排序集合（按交易id排序）
    private final ConcurrentSkipListSet<Transaction> sortedTransactions =
            new ConcurrentSkipListSet<>(Comparator.comparing(Transaction::getId));


    public Transaction findById(String id) {
        return storage.get(id);
    }

    public synchronized Transaction save(Transaction t) {
        // 重复ID检查
        if (storage.containsKey(t.getId())) {
            throw new DuplicateTransactionException(t.getId());
        }
        storage.put(t.getId(), t);
        sortedTransactions.add(t);
        return t;
    }

    public synchronized void delete(String id) {
        Transaction removed = storage.remove(id);
        if (removed == null) {
            throw new NoSuchElementException("Transaction not found: " + id);
        }
        sortedTransactions.remove(removed);
    }

    // 修改交易
    public Transaction update(String id, Transaction updatedTransaction) {
        // 1. 检查是否存在
        Transaction existing = storage.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Transaction not found: " + id);
        }

        // 2. 原子性更新（保证存储和排序集合的一致性）
        synchronized (this) {
            // 从排序集合中移除旧记录
            sortedTransactions.remove(existing);

            // 更新字段（允许部分更新）
            if (updatedTransaction.getAmount() != null) {
                existing.setAmount(updatedTransaction.getAmount());
            }
            if (updatedTransaction.getType() != null) {
                existing.setType(updatedTransaction.getType());
            }
            if (updatedTransaction.getDateTime() != null) {
                existing.setDateTime(updatedTransaction.getDateTime());
            }

            // 重新加入排序集合
            sortedTransactions.add(existing);
            storage.put(id, existing); // 覆盖原值
        }
        return existing;
    }

    public Page<Transaction> findAll(Pageable pageable) {
        List<Transaction> content = sortedTransactions.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, storage.size());
    }
}
