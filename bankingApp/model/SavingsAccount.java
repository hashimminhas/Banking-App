package bankingApp.model;

import java.math.BigDecimal;

/**
 * Savings Account implementation
 * Phase 2: Complete implementation with interest calculation
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
    public void calculateInterest() {
        BigDecimal interest = balance.multiply(INTEREST_RATE);
        balance = balance.add(interest);
    }
    
    @Override
    public String getAccountType() {
        return "Savings";
    }
    
    public BigDecimal getInterestRate() {
        return INTEREST_RATE;
    }
}