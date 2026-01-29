package com.greendaybank.dto;

import java.util.Map;

/**
 * Response DTO for balance endpoint
 */
public class BalanceResponse {
    private String user;
    private double cash;
    private double savingsBalance;
    private double investmentBalance;
    private Map<String, Double> funds;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public double getCash() {
        return cash;
    }
    
    public void setCash(double cash) {
        this.cash = cash;
    }
    
    public double getSavingsBalance() {
        return savingsBalance;
    }
    
    public void setSavingsBalance(double savingsBalance) {
        this.savingsBalance = savingsBalance;
    }
    
    public double getInvestmentBalance() {
        return investmentBalance;
    }
    
    public void setInvestmentBalance(double investmentBalance) {
        this.investmentBalance = investmentBalance;
    }
    
    public Map<String, Double> getFunds() {
        return funds;
    }
    
    public void setFunds(Map<String, Double> funds) {
        this.funds = funds;
    }
}
