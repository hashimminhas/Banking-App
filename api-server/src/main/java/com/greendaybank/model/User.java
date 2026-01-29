package com.greendaybank.model;

import java.math.BigDecimal;

/**
 * User class representing a bank customer
 */
public class User {
    private final String name;
    private BigDecimal cash;
    private final SavingsAccount savingsAccount;
    private final InvestmentAccount investmentAccount;
    
    public User(String name) {
        this.name = name;
        this.cash = new BigDecimal("1000"); // Each user starts with $1000 cash
        this.savingsAccount = new SavingsAccount();
        this.investmentAccount = new InvestmentAccount();
    }
    
    public String getName() {
        return name;
    }
    
    public BigDecimal getCash() {
        return cash;
    }
    
    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
    
    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }
    
    public InvestmentAccount getInvestmentAccount() {
        return investmentAccount;
    }
    
    public void depositCashToSavings(BigDecimal amount) {
        if (cash.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient cash on hand");
        }
        cash = cash.subtract(amount);
        savingsAccount.deposit(amount);
    }
    
    public void withdrawSavingsToCash(BigDecimal amount) {
        savingsAccount.withdraw(amount);
        cash = cash.add(amount);
    }
}
