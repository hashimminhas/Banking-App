package bankingApp.model;

import java.math.BigDecimal;

/**
 * Abstract base class for all account types
 * Phase 2: Complete implementation with interest calculation and account type methods
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
     * Calculate and apply interest to the account
     */
    public abstract void calculateInterest();
    
    /**
     * Get the type of account
     * @return Account type as string
     */
    public abstract String getAccountType();
    
    /**
     * Deposit money into the account
     * @param amount Amount to deposit
     */
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
        }
    }
    
    /**
     * Withdraw money from the account
     * @param amount Amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
}