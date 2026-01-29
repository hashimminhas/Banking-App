package bankingApp.model;

/**
 * Enum representing different investment fund types
 * Phase 1: Basic structure setup for future phases
 */
public enum Fund {
    LOW_RISK(0.02),    // 2% appreciation
    MEDIUM_RISK(0.05), // 5% appreciation  
    HIGH_RISK(0.10);   // 10% appreciation
    
    private final double appreciationRate;
    
    Fund(double appreciationRate) {
        this.appreciationRate = appreciationRate;
    }
    
    public double getAppreciationRate() {
        return appreciationRate;
    }
    
    @Override
    public String toString() {
        return name() + " (" + (appreciationRate * 100) + "%)";
    }
}