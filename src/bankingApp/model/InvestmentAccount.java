package bankingApp.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Investment Account implementation
 * Phase 2: Complete implementation with fund management and interest calculation
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
    public void calculateInterest() {
        // Calculate appreciation on all fund investments
        for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
            Fund fund = entry.getKey();
            BigDecimal currentAmount = entry.getValue();
            if (currentAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal appreciation = currentAmount.multiply(fund.getAppreciationRate());
                investments.put(fund, currentAmount.add(appreciation));
            }
        }
    }
    
    @Override
    public String getAccountType() {
        return "Investment";
    }
    
    /**
     * Invest money from account balance into a specific fund
     * @param fund The fund to invest in
     * @param amount Amount to invest
     * @return true if investment successful, false otherwise
     */
    public boolean investInFund(Fund fund, BigDecimal amount) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0) {
                balance = balance.subtract(amount);
                BigDecimal currentInvestment = investments.get(fund);
                investments.put(fund, currentInvestment.add(amount));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Withdraw all investments back to account balance
     * @return Total amount withdrawn from investments
     */
    public BigDecimal withdrawAllInvestments() {
        try {
            BigDecimal totalWithdrawn = BigDecimal.ZERO;
            for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
                BigDecimal amount = entry.getValue();
                totalWithdrawn = totalWithdrawn.add(amount);
                investments.put(entry.getKey(), BigDecimal.ZERO);
            }
            balance = balance.add(totalWithdrawn);
            return totalWithdrawn;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Get total value of all investments
     * @return Total investment value across all funds
     */
    public BigDecimal getTotalInvestmentValue() {
        return investments.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get investment amount in a specific fund
     * @param fund The fund to check
     * @return Amount invested in the fund
     */
    public BigDecimal getInvestmentInFund(Fund fund) {
        return investments.get(fund);
    }
    
    public Map<Fund, BigDecimal> getAllInvestments() {
        return new HashMap<>(investments);
    }
}