package com.greendaybank.model;

import java.math.BigDecimal;

/**
 * Abstract base class for all account types
 */
public abstract class Account {
    protected BigDecimal balance;
    
    public Account() {
        this.balance = BigDecimal.ZERO;
    }
    
    public Account(BigDecimal initialBalance) {
        this.balance = initialBalance;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        balance = balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
    
    /**
     * Calculate and apply interest/appreciation to the account
     */
    public abstract void calculateInterest();
    
    public abstract String getAccountType();
}
