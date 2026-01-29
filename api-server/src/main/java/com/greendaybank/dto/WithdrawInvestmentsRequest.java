package com.greendaybank.dto;

/**
 * Request DTO for withdraw all investments
 */
public class WithdrawInvestmentsRequest {
    private String user;
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
}
