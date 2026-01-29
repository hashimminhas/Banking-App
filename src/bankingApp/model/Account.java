package bankingApp.model;

import java.math.BigDecimal;

/**
 * Abstract base class for all account types
 * Phase 1: Basic structure setup for future phases
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
    
    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    /**
     * Deposit money into the account
     * @param amount Amount to deposit
     */
    public abstract void deposit(BigDecimal amount);
    
    /**
     * Withdraw money from the account
     * @param amount Amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    public abstract boolean withdraw(BigDecimal amount);
}