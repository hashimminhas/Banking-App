package bankingApp.model;

import java.math.BigDecimal;

/**
 * Savings Account implementation
 * Phase 1: Basic structure setup for future phases
 */
public class SavingsAccount extends Account {
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.01"); // 1%
    
    public SavingsAccount() {
        super();
    }
    
    public SavingsAccount(BigDecimal initialBalance) {
        super(initialBalance);
    }
    
    @Override
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    @Override
    public boolean withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
    
    /**
     * Apply interest to the savings account
     */
    public void applyInterest() {
        BigDecimal interest = balance.multiply(INTEREST_RATE);
        balance = balance.add(interest);
    }
    
    public BigDecimal getInterestRate() {
        return INTEREST_RATE;
    }
}