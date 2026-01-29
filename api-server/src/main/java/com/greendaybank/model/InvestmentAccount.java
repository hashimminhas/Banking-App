package com.greendaybank.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Investment Account with fund management
 */
public class InvestmentAccount extends Account {
    private final Map<Fund, BigDecimal> investments;
    
    public InvestmentAccount() {
        super();
        this.investments = new EnumMap<>(Fund.class);
        for (Fund fund : Fund.values()) {
            investments.put(fund, BigDecimal.ZERO);
        }
    }
    
    public InvestmentAccount(BigDecimal initialBalance) {
        super(initialBalance);
        this.investments = new EnumMap<>(Fund.class);
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
    
    public boolean investInFund(Fund fund, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || balance.compareTo(amount) < 0) {
            return false;
        }
        balance = balance.subtract(amount);
        BigDecimal currentInvestment = investments.get(fund);
        investments.put(fund, currentInvestment.add(amount));
        return true;
    }
    
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
    
    public BigDecimal getInvestmentInFund(Fund fund) {
        return investments.get(fund);
    }
    
    public Map<Fund, BigDecimal> getAllInvestments() {
        return new EnumMap<>(investments);
    }
}
