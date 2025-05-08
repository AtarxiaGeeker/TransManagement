package com.example.trans.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易实体
 */
public class Transaction {
    /** 交易id */
    private String id;
    /** 交易类型: DEPOSIT, WITHDRAWAL, TRANSFER */
    @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER)$")
    private String type;
    /** 交易金额 */
    @Positive(message = "金额必须为正数")
    private BigDecimal amount;
    /** 交易时间 */
    private LocalDateTime dateTime;

    public Transaction() {
    }

    public Transaction(String id, String type, BigDecimal amount, LocalDateTime dateTime) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER)$") String getType() {
        return type;
    }

    public void setType(@Pattern(regexp = "^(DEPOSIT|WITHDRAWAL|TRANSFER)$") String type) {
        this.type = type;
    }

    public @Positive(message = "金额必须为正数") BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "金额必须为正数") BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
