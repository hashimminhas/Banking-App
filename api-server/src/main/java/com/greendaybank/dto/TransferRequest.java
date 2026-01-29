package com.greendaybank.dto;

import java.math.BigDecimal;

/**
 * Request DTO for transfer between accounts
 */
public class TransferRequest {
    private String user;
    private String direction; // SAVINGS_TO_INVESTMENT or INVESTMENT_TO_SAVINGS
    private BigDecimal amount;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
