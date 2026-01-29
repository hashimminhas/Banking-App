package bankingApp;

import bankingApp.model.User;
import bankingApp.model.Fund;
import bankingApp.exception.InvalidAmountException;
import java.math.BigDecimal;

/**
 * Test class for Phase 3 implementation
 * Tests custom exception handling and validation
 */
public class Phase3Test {
    
    public static void main(String[] args) {
        System.out.println("=== Green Day Bank - Phase 3 Testing ===\n");
        
        // Create test user
        User alice = new User("Alice");
        System.out.println("1. Initial State:");
        System.out.println("User: " + alice.getName());
        System.out.println("Cash: $" + alice.getCash());
        System.out.println("Savings: $" + alice.getSavingsAccount().getBalance());
        
        // Test positive amount validation
        System.out.println("\n2. Testing Negative Amount Validation:");
        try {
            alice.getSavingsAccount().deposit(new BigDecimal("-50"));
            System.out.println("ERROR: Negative deposit should have failed!");
        } catch (InvalidAmountException e) {
            System.out.println("✓ Negative deposit correctly rejected: " + e.getMessage());
        }
        
        try {
            alice.getSavingsAccount().withdraw(new BigDecimal("-100"));
            System.out.println("ERROR: Negative withdrawal should have failed!");
        } catch (InvalidAmountException e) {
            System.out.println("✓ Negative withdrawal correctly rejected: " + e.getMessage());
        }
        
        // Test zero amount validation
        System.out.println("\n3. Testing Zero Amount Validation:");
        try {
            alice.getSavingsAccount().deposit(BigDecimal.ZERO);
            System.out.println("ERROR: Zero deposit should have failed!");
        } catch (InvalidAmountException e) {
            System.out.println("✓ Zero deposit correctly rejected: " + e.getMessage());
        }
        
        try {
            alice.getSavingsAccount().withdraw(BigDecimal.ZERO);
            System.out.println("ERROR: Zero withdrawal should have failed!");
        } catch (InvalidAmountException e) {
            System.out.println("✓ Zero withdrawal correctly rejected: " + e.getMessage());
        }
        
        // Test insufficient funds validation
        System.out.println("\n4. Testing Insufficient Funds Validation:");
        try {
            alice.getSavingsAccount().withdraw(new BigDecimal("2000"));
            System.out.println("ERROR: Insufficient funds withdrawal should have failed!");
        } catch (InvalidAmountException e) {
            System.out.println("✓ Insufficient funds withdrawal correctly rejected: " + e.getMessage());
        }
        
        // Test successful operations
        System.out.println("\n5. Testing Valid Operations:");
        try {
            alice.getSavingsAccount().deposit(new BigDecimal("100"));
            System.out.println("✓ Valid deposit successful. Savings: $" + alice.getSavingsAccount().getBalance());
            
            alice.getSavingsAccount().withdraw(new BigDecimal("30"));
            System.out.println("✓ Valid withdrawal successful. Savings: $" + alice.getSavingsAccount().getBalance());
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: Valid operation failed: " + e.getMessage());
        }
        
        // Test User class operations with exception handling
        System.out.println("\n6. Testing User Class Exception Handling:");
        
        // Test invalid cash to savings deposit
        boolean result1 = alice.depositCashToSavings(new BigDecimal("-50"));
        System.out.println("Attempted negative cash deposit: " + result1 + " (should be false)");
        
        boolean result2 = alice.depositCashToSavings(new BigDecimal("5000"));
        System.out.println("Attempted excessive cash deposit: " + result2 + " (should be false - insufficient cash)");
        
        boolean result3 = alice.depositCashToSavings(new BigDecimal("200"));
        System.out.println("Valid cash deposit: " + result3 + " (should be true)");
        System.out.println("Cash after deposit: $" + alice.getCash());
        System.out.println("Savings after deposit: $" + alice.getSavingsAccount().getBalance());
        
        // Test withdrawal with exception handling
        boolean result4 = alice.withdrawSavingsToCash(new BigDecimal("5000"));
        System.out.println("Attempted excessive withdrawal: " + result4 + " (should be false)");
        
        boolean result5 = alice.withdrawSavingsToCash(new BigDecimal("50"));
        System.out.println("Valid withdrawal: " + result5 + " (should be true)");
        System.out.println("Cash after withdrawal: $" + alice.getCash());
        System.out.println("Savings after withdrawal: $" + alice.getSavingsAccount().getBalance());
        
        // Test investment account operations
        System.out.println("\n7. Testing Investment Account Exception Handling:");
        try {
            alice.getInvestmentAccount().deposit(new BigDecimal("100"));
            System.out.println("Investment account deposit: $" + alice.getInvestmentAccount().getBalance());
            
            boolean investResult = alice.getInvestmentAccount().investInFund(Fund.HIGH_RISK, new BigDecimal("50"));
            System.out.println("Fund investment result: " + investResult);
            System.out.println("Investment balance after fund investment: $" + alice.getInvestmentAccount().getBalance());
            System.out.println("HIGH_RISK fund amount: $" + alice.getInvestmentAccount().getInvestmentInFund(Fund.HIGH_RISK));
            
        } catch (InvalidAmountException e) {
            System.out.println("Investment operation error: " + e.getMessage());
        }
        
        System.out.println("\n8. Final State Verification:");
        System.out.println("Final Cash: $" + alice.getCash());
        System.out.println("Final Savings: $" + alice.getSavingsAccount().getBalance());
        System.out.println("Final Investment: $" + alice.getInvestmentAccount().getBalance());
        
        System.out.println("\n=== Phase 3 Exception Testing Complete ===");
        
        // Test direct exception throwing
        System.out.println("\n9. Direct Exception Testing:");
        testDirectExceptions(alice);
    }
    
    private static void testDirectExceptions(User user) {
        System.out.println("Testing direct exception scenarios:");
        
        // Test Case 1: Negative deposit
        try {
            user.getSavingsAccount().deposit(new BigDecimal("-10"));
        } catch (InvalidAmountException e) {
            System.out.println("✓ Caught negative deposit: " + e.getMessage());
        }
        
        // Test Case 2: Insufficient funds withdrawal
        try {
            user.getSavingsAccount().withdraw(new BigDecimal("10000"));
        } catch (InvalidAmountException e) {
            System.out.println("✓ Caught insufficient funds: " + e.getMessage());
        }
        
        // Test Case 3: Zero amount operations
        try {
            user.getInvestmentAccount().deposit(BigDecimal.ZERO);
        } catch (InvalidAmountException e) {
            System.out.println("✓ Caught zero amount: " + e.getMessage());
        }
        
        System.out.println("Direct exception testing completed successfully!");
    }
}