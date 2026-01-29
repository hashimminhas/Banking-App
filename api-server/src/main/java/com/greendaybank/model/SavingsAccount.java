package com.greendaybank.model;

import java.math.BigDecimal;

/**
 * Savings Account with 1% interest rate
 */
public class SavingsAccount extends Account {
    private final BigDecimal INTEREST_RATE = new BigDecimal("0.01"); // 1%
    
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
}
