package com.greendaybank.dto;

/**
 * Request DTO for balance endpoint
 */
public class BalanceRequest {
    private String user;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
}
