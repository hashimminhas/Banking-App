package bankingApp;

import bankingApp.model.User;
import bankingApp.model.Fund;
import bankingApp.exception.InvalidAmountException;
import java.math.BigDecimal;

/**
 * Test class for Phase 2 implementation
 * Tests User model, Account inheritance, and Fund operations
 * Updated for Phase 3 exception handling
 */
public class Phase2Test {
    
    public static void main(String[] args) {
        System.out.println("=== Green Day Bank - Phase 2 Testing (Updated for Phase 3) ===\n");
        
        try {
            // Test User creation with initial cash
            User alice = new User("Alice");
            System.out.println("1. User Creation Test:");
            System.out.println("User: " + alice.getName());
            System.out.println("Cash: $" + alice.getCash());
            System.out.println("Savings Balance: $" + alice.getSavingsAccount().getBalance());
            System.out.println("Investment Balance: $" + alice.getInvestmentAccount().getBalance());
            System.out.println("Savings Account Type: " + alice.getSavingsAccount().getAccountType());
            System.out.println("Investment Account Type: " + alice.getInvestmentAccount().getAccountType());
            
            System.out.println("\n2. Deposit Test:");
            // Test deposit from cash to savings
            BigDecimal depositAmount = new BigDecimal("200");
            boolean depositSuccess = alice.depositCashToSavings(depositAmount);
            System.out.println("Deposited $" + depositAmount + " to savings: " + depositSuccess);
            System.out.println("Cash after deposit: $" + alice.getCash());
            System.out.println("Savings after deposit: $" + alice.getSavingsAccount().getBalance());
            
            System.out.println("\n3. Interest Calculation Test:");
            // Test interest calculation
            alice.getSavingsAccount().calculateInterest();
            System.out.println("Savings after 1% interest: $" + alice.getSavingsAccount().getBalance());
            
            System.out.println("\n4. Investment Account Test:");
            // Transfer money to investment account
            BigDecimal transferAmount = new BigDecimal("100");
            alice.getInvestmentAccount().deposit(transferAmount);
            System.out.println("Investment balance after $" + transferAmount + " deposit: $" + 
                              alice.getInvestmentAccount().getBalance());
            
            // Test fund investment
            BigDecimal investAmount = new BigDecimal("50");
            boolean investSuccess = alice.getInvestmentAccount().investInFund(Fund.MEDIUM_RISK, investAmount);
            System.out.println("Invested $" + investAmount + " in MEDIUM_RISK fund: " + investSuccess);
            System.out.println("Investment account balance after fund investment: $" + 
                              alice.getInvestmentAccount().getBalance());
            System.out.println("Amount in MEDIUM_RISK fund: $" + 
                              alice.getInvestmentAccount().getInvestmentInFund(Fund.MEDIUM_RISK));
            
            System.out.println("\n5. Fund Appreciation Test:");
            // Test fund appreciation
            alice.getInvestmentAccount().calculateInterest();
            System.out.println("MEDIUM_RISK fund after 5% appreciation: $" + 
                              alice.getInvestmentAccount().getInvestmentInFund(Fund.MEDIUM_RISK));
            
            System.out.println("\n6. Fund Types Test:");
            for (Fund fund : Fund.values()) {
                System.out.println(fund + " - Rate: " + fund.getAppreciationRate());
            }
            
            System.out.println("\n7. Withdraw All Investments Test:");
            BigDecimal totalWithdrawn = alice.getInvestmentAccount().withdrawAllInvestments();
            System.out.println("Total withdrawn from investments: $" + totalWithdrawn);
            System.out.println("Investment account balance after withdrawal: $" + 
                              alice.getInvestmentAccount().getBalance());
            System.out.println("Amount remaining in MEDIUM_RISK fund: $" + 
                              alice.getInvestmentAccount().getInvestmentInFund(Fund.MEDIUM_RISK));
            
            System.out.println("\n8. Final State:");
            System.out.println("Final Cash: $" + alice.getCash());
            System.out.println("Final Savings: $" + alice.getSavingsAccount().getBalance());
            System.out.println("Final Investment: $" + alice.getInvestmentAccount().getBalance());
            System.out.println("Total Investment Value: $" + alice.getInvestmentAccount().getTotalInvestmentValue());
            
            System.out.println("\n=== Phase 2 Testing Complete ===");
            
        } catch (InvalidAmountException e) {
            System.out.println("Exception occurred during testing: " + e.getMessage());
        }
    }
}