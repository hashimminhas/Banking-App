package com.greendaybank.dto;

import java.math.BigDecimal;

/**
 * Request DTO for deposit/withdraw operations
 */
public class AmountRequest {
    private String user;
    private BigDecimal amount;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
