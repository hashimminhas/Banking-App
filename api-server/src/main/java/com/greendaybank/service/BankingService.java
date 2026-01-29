package com.greendaybank.service;

import com.greendaybank.dto.BalanceResponse;
import com.greendaybank.model.Fund;
import com.greendaybank.model.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Banking service handling all business logic
 */
public class BankingService {
    private final Map<String, User> users;
    
    public BankingService() {
        this.users = new LinkedHashMap<>();
        // Initialize the 4 users
        Arrays.asList("Alice", "Bob", "Charlie", "Diana")
              .forEach(name -> users.put(name, new User(name)));
    }
    
    public List<String> getAllUserNames() {
        return Arrays.asList("Alice", "Bob", "Charlie", "Diana");
    }
    
    public User getUser(String name) {
        return users.get(name);
    }
    
    public boolean userExists(String name) {
        return users.containsKey(name);
    }
    
    /**
     * Get balance with interest applied
     */
    public BalanceResponse getBalance(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Apply interest before returning balance
        user.getSavingsAccount().calculateInterest();
        user.getInvestmentAccount().calculateInterest();
        
        BalanceResponse response = new BalanceResponse();
        response.setUser(username);
        response.setCash(toDouble(user.getCash()));
        response.setSavingsBalance(toDouble(user.getSavingsAccount().getBalance()));
        response.setInvestmentBalance(toDouble(user.getInvestmentAccount().getBalance()));
        
        // Build funds map in enum order
        Map<String, Double> fundsMap = new LinkedHashMap<>();
        for (Fund fund : Fund.values()) {
            BigDecimal fundAmount = user.getInvestmentAccount().getInvestmentInFund(fund);
            fundsMap.put(fund.name(), toDouble(fundAmount));
        }
        response.setFunds(fundsMap);
        
        return response;
    }
    
    /**
     * Deposit cash to savings account
     */
    public void deposit(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        user.depositCashToSavings(amount);
    }
    
    /**
     * Withdraw from savings to cash
     */
    public void withdraw(String username, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        user.withdrawSavingsToCash(amount);
    }
    
    /**
     * Send money from one user's savings to another user's savings
     */
    public void sendMoney(String fromUsername, String toUsername, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        if (fromUsername.equals(toUsername)) {
            throw new IllegalArgumentException("Cannot send money to yourself");
        }
        
        User fromUser = users.get(fromUsername);
        User toUser = users.get(toUsername);
        
        if (fromUser == null || toUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // Withdraw from sender's savings
        fromUser.getSavingsAccount().withdraw(amount);
        
        // Deposit to receiver's savings
        toUser.getSavingsAccount().deposit(amount);
    }
    
    /**
     * Transfer between accounts for the same user
     */
    public void transfer(String username, String direction, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if ("SAVINGS_TO_INVESTMENT".equals(direction)) {
            user.getSavingsAccount().withdraw(amount);
            user.getInvestmentAccount().deposit(amount);
        } else if ("INVESTMENT_TO_SAVINGS".equals(direction)) {
            user.getInvestmentAccount().withdraw(amount);
            user.getSavingsAccount().deposit(amount);
        } else {
            throw new IllegalArgumentException("Invalid transfer direction");
        }
    }
    
    /**
     * Invest in a specific fund
     */
    public void invest(String username, String fundName, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        Fund fund;
        try {
            fund = Fund.valueOf(fundName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid fund name");
        }
        
        boolean success = user.getInvestmentAccount().investInFund(fund, amount);
        if (!success) {
            throw new IllegalArgumentException("Insufficient funds in investment account");
        }
    }
    
    /**
     * Withdraw all investments back to investment account balance
     */
    public void withdrawAllInvestments(String username) {
        User user = users.get(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        user.getInvestmentAccount().withdrawAllInvestments();
    }
    
    /**
     * Convert BigDecimal to double with 2 decimal places
     */
    private double toDouble(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
