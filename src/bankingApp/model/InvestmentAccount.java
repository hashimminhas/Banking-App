package bankingApp.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Investment Account implementation
 * Phase 1: Basic structure setup for future phases
 */
public class InvestmentAccount extends Account {
    private Map<Fund, BigDecimal> investments;
    
    public InvestmentAccount() {
        super();
        this.investments = new HashMap<>();
        // Initialize all funds with zero investment
        for (Fund fund : Fund.values()) {
            investments.put(fund, BigDecimal.ZERO);
        }
    }
    
    public InvestmentAccount(BigDecimal initialBalance) {
        super(initialBalance);
        this.investments = new HashMap<>();
        // Initialize all funds with zero investment
        for (Fund fund : Fund.values()) {
            investments.put(fund, BigDecimal.ZERO);
        }
    }
    
    @Override
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    @Override
    public boolean withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            return true;
        }
        return false;
    }
    
    /**
     * Invest money from account balance into a specific fund
     * @param fund The fund to invest in
     * @param amount Amount to invest
     * @return true if investment successful, false otherwise
     */
    public boolean investInFund(Fund fund, BigDecimal amount) {
        if (balance.compareTo(amount) >= 0) {
            balance = balance.subtract(amount);
            BigDecimal currentInvestment = investments.get(fund);
            investments.put(fund, currentInvestment.add(amount));
            return true;
        }
        return false;
    }
    
    /**
     * Get investment amount in a specific fund
     * @param fund The fund to check
     * @return Amount invested in the fund
     */
    public BigDecimal getInvestmentInFund(Fund fund) {
        return investments.get(fund);
    }
    
    /**
     * Apply appreciation to all fund investments
     */
    public void applyFundAppreciation() {
        for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
            Fund fund = entry.getKey();
            BigDecimal currentAmount = entry.getValue();
            if (currentAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal appreciation = currentAmount.multiply(
                    new BigDecimal(fund.getAppreciationRate())
                );
                investments.put(fund, currentAmount.add(appreciation));
            }
        }
    }
    
    /**
     * Withdraw all investments back to account balance
     * @return Total amount withdrawn from investments
     */
    public BigDecimal withdrawAllInvestments() {
        BigDecimal totalWithdrawn = BigDecimal.ZERO;
        for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
            BigDecimal amount = entry.getValue();
            totalWithdrawn = totalWithdrawn.add(amount);
            investments.put(entry.getKey(), BigDecimal.ZERO);
        }
        balance = balance.add(totalWithdrawn);
        return totalWithdrawn;
    }
    
    /**
     * Get total amount invested across all funds
     * @return Total investment amount
     */
    public BigDecimal getTotalInvestments() {
        return investments.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public Map<Fund, BigDecimal> getAllInvestments() {
        return new HashMap<>(investments);
    }
}