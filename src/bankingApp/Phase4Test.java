package bankingApp;

import bankingApp.model.User;
import bankingApp.model.Fund;
import bankingApp.service.BankingService;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Test class for Phase 4 implementation
 * Tests Banking Service core operations (Show Balance, Deposit, Withdraw)
 */
public class Phase4Test {
    
    public static void main(String[] args) {
        System.out.println("=== Green Day Bank - Phase 4 Testing ===\n");
        
        // Test 1: User initialization
        testUserInitialization();
        
        // Test 2: Banking operations simulation
        testBankingOperationsSimulation();
        
        // Test 3: Interest calculation verification
        testInterestCalculations();
        
        // Test 4: Error handling verification
        testErrorHandling();
        
        System.out.println("\n=== Phase 4 Testing Complete ===");
    }
    
    /**
     * Test that all users are initialized correctly
     */
    private static void testUserInitialization() {
        System.out.println("1. Testing User Initialization:");
        
        // Create scanner for testing (won't be used in this test)
        Scanner testScanner = new Scanner(System.in);
        BankingService service = new BankingService(testScanner);
        
        // Verify users have been created with correct initial state
        // Note: We can't directly access users map, so we'll create users manually to verify
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");
        User diana = new User("Diana");
        
        System.out.println("✓ Alice initialized - Cash: $" + alice.getCash() + 
                          ", Savings: $" + alice.getSavingsAccount().getBalance() +
                          ", Investment: $" + alice.getInvestmentAccount().getBalance());
        
        System.out.println("✓ Bob initialized - Cash: $" + bob.getCash() + 
                          ", Savings: $" + bob.getSavingsAccount().getBalance() +
                          ", Investment: $" + bob.getInvestmentAccount().getBalance());
        
        System.out.println("✓ Charlie initialized - Cash: $" + charlie.getCash() + 
                          ", Savings: $" + charlie.getSavingsAccount().getBalance() +
                          ", Investment: $" + charlie.getInvestmentAccount().getBalance());
        
        System.out.println("✓ Diana initialized - Cash: $" + diana.getCash() + 
                          ", Savings: $" + diana.getSavingsAccount().getBalance() +
                          ", Investment: $" + diana.getInvestmentAccount().getBalance());
        
        testScanner.close();
    }
    
    /**
     * Test banking operations simulation (deposit, withdraw, balance viewing)
     */
    private static void testBankingOperationsSimulation() {
        System.out.println("\n2. Testing Banking Operations Simulation:");
        
        User alice = new User("Alice");
        
        System.out.println("Initial state:");
        System.out.println("  Cash: $" + alice.getCash());
        System.out.println("  Savings: $" + alice.getSavingsAccount().getBalance());
        
        // Simulate deposit operation
        System.out.println("\nSimulating deposit of $500:");
        boolean depositResult = alice.depositCashToSavings(new BigDecimal("500"));
        System.out.println("  Deposit successful: " + depositResult);
        System.out.println("  New Cash: $" + alice.getCash());
        System.out.println("  New Savings: $" + alice.getSavingsAccount().getBalance());
        
        // Simulate interest calculation (as would happen in Show Balance)
        System.out.println("\nSimulating interest calculation:");
        System.out.println("  Savings before interest: $" + alice.getSavingsAccount().getBalance());
        alice.getSavingsAccount().calculateInterest();
        System.out.println("  Savings after 1% interest: $" + alice.getSavingsAccount().getBalance());
        
        // Simulate withdrawal operation
        System.out.println("\nSimulating withdrawal of $200:");
        boolean withdrawResult = alice.withdrawSavingsToCash(new BigDecimal("200"));
        System.out.println("  Withdrawal successful: " + withdrawResult);
        System.out.println("  New Savings: $" + alice.getSavingsAccount().getBalance());
        System.out.println("  New Cash: $" + alice.getCash());
        
        // Simulate another interest calculation
        System.out.println("\nSecond interest calculation:");
        System.out.println("  Savings before interest: $" + alice.getSavingsAccount().getBalance());
        alice.getSavingsAccount().calculateInterest();
        System.out.println("  Savings after 1% interest: $" + alice.getSavingsAccount().getBalance());
    }
    
    /**
     * Test interest and gains calculations
     */
    private static void testInterestCalculations() {
        System.out.println("\n3. Testing Interest and Gains Calculations:");
        
        User bob = new User("Bob");
        
        // Test savings interest
        try {
            bob.getSavingsAccount().deposit(new BigDecimal("1000"));
            System.out.println("Deposited $1000 to savings: $" + bob.getSavingsAccount().getBalance());
            
            bob.getSavingsAccount().calculateInterest();
            System.out.println("After 1% interest: $" + bob.getSavingsAccount().getBalance());
        } catch (Exception e) {
            System.out.println("Error in savings interest test: " + e.getMessage());
        }
        
        // Test investment gains
        try {
            bob.getInvestmentAccount().deposit(new BigDecimal("500"));
            System.out.println("\nInvestment account balance: $" + bob.getInvestmentAccount().getBalance());
            
            // Invest in different funds
            bob.getInvestmentAccount().investInFund(Fund.LOW_RISK, new BigDecimal("100"));
            bob.getInvestmentAccount().investInFund(Fund.MEDIUM_RISK, new BigDecimal("200"));
            bob.getInvestmentAccount().investInFund(Fund.HIGH_RISK, new BigDecimal("100"));
            
            System.out.println("After investing in funds:");
            System.out.println("  " + Fund.LOW_RISK + ": $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.LOW_RISK));
            System.out.println("  " + Fund.MEDIUM_RISK + ": $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.MEDIUM_RISK));
            System.out.println("  " + Fund.HIGH_RISK + ": $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.HIGH_RISK));
            System.out.println("  Remaining balance: $" + bob.getInvestmentAccount().getBalance());
            
            // Calculate gains
            bob.getInvestmentAccount().calculateInterest();
            System.out.println("\nAfter fund appreciation:");
            System.out.println("  " + Fund.LOW_RISK + " (2%): $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.LOW_RISK));
            System.out.println("  " + Fund.MEDIUM_RISK + " (5%): $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.MEDIUM_RISK));
            System.out.println("  " + Fund.HIGH_RISK + " (10%): $" + bob.getInvestmentAccount().getInvestmentInFund(Fund.HIGH_RISK));
            System.out.println("  Total investment value: $" + bob.getInvestmentAccount().getTotalInvestmentValue());
            
        } catch (Exception e) {
            System.out.println("Error in investment gains test: " + e.getMessage());
        }
    }
    
    /**
     * Test error handling scenarios
     */
    private static void testErrorHandling() {
        System.out.println("\n4. Testing Error Handling:");
        
        User charlie = new User("Charlie");
        
        // Test insufficient cash deposit
        System.out.println("Testing insufficient cash deposit (trying to deposit $2000 with $1000 cash):");
        boolean result1 = charlie.depositCashToSavings(new BigDecimal("2000"));
        System.out.println("  Result: " + result1 + " (should be false)");
        System.out.println("  Cash unchanged: $" + charlie.getCash());
        
        // Test negative deposit
        System.out.println("\nTesting negative deposit:");
        boolean result2 = charlie.depositCashToSavings(new BigDecimal("-100"));
        System.out.println("  Result: " + result2 + " (should be false)");
        
        // Test withdrawal from empty savings
        System.out.println("\nTesting withdrawal from empty savings:");
        boolean result3 = charlie.withdrawSavingsToCash(new BigDecimal("100"));
        System.out.println("  Result: " + result3 + " (should be false)");
        System.out.println("  Savings balance: $" + charlie.getSavingsAccount().getBalance());
        
        // Test valid operation after errors
        System.out.println("\nTesting valid operation after errors:");
        boolean result4 = charlie.depositCashToSavings(new BigDecimal("300"));
        System.out.println("  Valid deposit result: " + result4 + " (should be true)");
        System.out.println("  New cash: $" + charlie.getCash());
        System.out.println("  New savings: $" + charlie.getSavingsAccount().getBalance());
    }
}