package com.greendaybank.dto;

import java.util.Map;

/**
 * Response DTO for health check
 */
public class HealthResponse {
    private String status;
    
    public HealthResponse(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
