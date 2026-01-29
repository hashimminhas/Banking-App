package bankingApp.model;

import java.math.BigDecimal;

/**
 * User class representing a bank customer
 * Phase 1: Basic structure setup for future phases
 */
public class User {
    private String name;
    private BigDecimal cash;
    private SavingsAccount savingsAccount;
    private InvestmentAccount investmentAccount;
    
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
    
    /**
     * Deposit cash into savings account
     * @param amount Amount to deposit
     * @return true if deposit successful, false otherwise
     */
    public boolean depositCashToSavings(BigDecimal amount) {
        if (cash.compareTo(amount) >= 0) {
            cash = cash.subtract(amount);
            savingsAccount.deposit(amount);
            return true;
        }
        return false;
    }
    
    /**
     * Withdraw from savings account to cash
     * @param amount Amount to withdraw
     * @return true if withdrawal successful, false otherwise
     */
    public boolean withdrawSavingsToCash(BigDecimal amount) {
        if (savingsAccount.withdraw(amount)) {
            cash = cash.add(amount);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "User: " + name + ", Cash: $" + cash;
    }
}