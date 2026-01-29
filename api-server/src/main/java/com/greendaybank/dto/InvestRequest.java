package com.greendaybank.dto;

import java.math.BigDecimal;

/**
 * Request DTO for investing in funds
 */
public class InvestRequest {
    private String user;
    private String fund; // LOW_RISK, MEDIUM_RISK, or HIGH_RISK
    private BigDecimal amount;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getFund() {
        return fund;
    }
    
    public void setFund(String fund) {
        this.fund = fund;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
