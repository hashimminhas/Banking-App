package bankingApp.model;

import java.math.BigDecimal;

/**
 * Enum representing different investment fund types
 * Phase 2: Complete implementation with BigDecimal appreciation rates
 */
public enum Fund {
    LOW_RISK(new BigDecimal("0.02")),    // 2% appreciation
    MEDIUM_RISK(new BigDecimal("0.05")), // 5% appreciation  
    HIGH_RISK(new BigDecimal("0.10"));   // 10% appreciation
    
    private final BigDecimal appreciationRate;
    
    Fund(BigDecimal appreciationRate) {
        this.appreciationRate = appreciationRate;
    }
    
    public BigDecimal getAppreciationRate() {
        return appreciationRate;
    }
    
    @Override
    public String toString() {
        return name() + " (" + appreciationRate.multiply(new BigDecimal("100")) + "%)";
    }
}