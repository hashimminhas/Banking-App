package bankingApp.service;

import bankingApp.model.User;
import bankingApp.model.Fund;
import bankingApp.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Banking Service handles all CLI interactions and banking logic
 * Phase 4: Complete implementation with core banking operations
 */
public class BankingService {
    
    private final Scanner scanner;
    private final Map<String, User> users;
    private User currentUser;
    private boolean isLoggedIn;
    
    public BankingService(Scanner scanner) {
        this.scanner = scanner;
        this.users = new HashMap<>();
        this.currentUser = null;
        this.isLoggedIn = false;
        
        // Initialize all 4 users with $1000 cash each
        initializeUsers();
    }
    
    /**
     * Initialize the 4 users for the banking system
     */
    private void initializeUsers() {
        users.put("Alice", new User("Alice"));
        users.put("Bob", new User("Bob"));
        users.put("Charlie", new User("Charlie"));
        users.put("Diana", new User("Diana"));
    }
    
    /**
     * Main application loop
     */
    public void start() {
        System.out.println("Welcome to Green Day Bank!");
        
        while (true) {
            if (!isLoggedIn) {
                if (!login()) {
                    break; // EOF encountered during login
                }
            } else {
                if (!showMenuAndHandleChoice()) {
                    break; // User chose to exit or EOF encountered
                }
            }
        }
        
        System.out.println("Thank you for using Green Day Bank!");
    }
    
    /**
     * Handle user login process
     * @return false if EOF encountered, true otherwise
     */
    private boolean login() {
        System.out.print("Please enter your name to login: ");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            return false;
        }
        
        String username = scanner.nextLine().trim();
        
        if (users.containsKey(username)) {
            currentUser = users.get(username);
            isLoggedIn = true;
            System.out.println("Welcome, " + username + "!");
            return true;
        } else {
            System.out.println("Invalid username. Please try again.");
            System.out.println("Valid users are: Alice, Bob, Charlie, Diana");
            return true;
        }
    }
    
    /**
     * Display menu and handle user choice
     * @return false if user chooses to exit or EOF encountered, true otherwise
     */
    private boolean showMenuAndHandleChoice() {
        displayMenu();
        
        System.out.print("Please select an option: ");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            return false;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            int choice = Integer.parseInt(input);
            return handleMenuChoice(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 9.");
            return true;
        }
    }
    
    /**
     * Display the main banking menu
     */
    private void displayMenu() {
        System.out.println("\n --- Banking App Menu ---");
        System.out.println("1. Show balance");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Send money to a person");
        System.out.println("5. Invest in funds");
        System.out.println("6. Transfer between accounts");
        System.out.println("7. Withdraw all investments");
        System.out.println("8. Logout");
        System.out.println("9. Exit");
    }
    
    /**
     * Handle the selected menu choice
     * @param choice The menu option selected by user
     * @return false if user chooses to exit, true otherwise
     */
    private boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                showBalance();
                break;
            case 2:
                depositMoney();
                break;
            case 3:
                withdrawMoney();
                break;
            case 4:
                sendMoney();
                break;
            case 5:
                investInFunds();
                break;
            case 6:
                transferBetweenAccounts();
                break;
            case 7:
                withdrawAllInvestments();
                break;
            case 8:
                logout();
                break;
            case 9:
                return false; // Exit application
            default:
                System.out.println("Invalid option. Please select a number between 1 and 9.");
                break;
        }
        return true;
    }
    
    /**
     * Menu Option 1: Show Balance
     * Displays cash, savings (with interest), investment (with gains), and fund details
     */
    private void showBalance() {
        System.out.println("\n=== Account Balance for " + currentUser.getName() + " ===");
        
        // Calculate interest on savings FIRST
        currentUser.getSavingsAccount().calculateInterest();
        
        // Calculate gains on investments FIRST  
        currentUser.getInvestmentAccount().calculateInterest();
        
        // Display cash on hand
        System.out.println("Cash on hand: $" + currentUser.getCash());
        
        // Display savings account balance
        System.out.println("Savings account balance: $" + currentUser.getSavingsAccount().getBalance());
        
        // Display investment account balance
        System.out.println("Investment account balance: $" + currentUser.getInvestmentAccount().getBalance());
        
        // Display total investments in each fund
        System.out.println("\nInvestment Fund Details:");
        for (Fund fund : Fund.values()) {
            BigDecimal fundAmount = currentUser.getInvestmentAccount().getInvestmentInFund(fund);
            System.out.println("  " + fund + ": $" + fundAmount);
        }
        
        // Display total investment value
        BigDecimal totalInvestments = currentUser.getInvestmentAccount().getTotalInvestmentValue();
        System.out.println("Total investments: $" + totalInvestments);
    }
    
    /**
     * Menu Option 2: Deposit Money
     * Transfers money from cash to savings account
     */
    private void depositMoney() {
        System.out.println("\n=== Deposit Money ===");
        System.out.println("Current cash on hand: $" + currentUser.getCash());
        
        System.out.print("Enter amount to deposit: $");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            // Attempt to deposit cash to savings
            boolean success = currentUser.depositCashToSavings(amount);
            
            if (success) {
                System.out.println("Successfully deposited $" + amount + " to your savings account.");
                System.out.println("New cash balance: $" + currentUser.getCash());
                System.out.println("New savings balance: $" + currentUser.getSavingsAccount().getBalance());
            } else {
                System.out.println("Deposit failed. Please ensure you have sufficient cash and enter a positive amount.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Menu Option 3: Withdraw Money  
     * Transfers money from savings to cash
     */
    private void withdrawMoney() {
        System.out.println("\n=== Withdraw Money ===");
        System.out.println("Current savings balance: $" + currentUser.getSavingsAccount().getBalance());
        
        System.out.print("Enter amount to withdraw: $");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            // Attempt to withdraw from savings to cash
            boolean success = currentUser.withdrawSavingsToCash(amount);
            
            if (success) {
                System.out.println("Successfully withdrew $" + amount + " from your savings account.");
                System.out.println("New savings balance: $" + currentUser.getSavingsAccount().getBalance());
                System.out.println("New cash balance: $" + currentUser.getCash());
            } else {
                System.out.println("Withdrawal failed. Please ensure you have sufficient funds and enter a positive amount.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Menu Option 4: Send Money to Person
     * Transfers money from current user's savings to another user's savings
     */
    private void sendMoney() {
        System.out.println("\n=== Send Money ===");
        
        // Show list of other users
        System.out.println("Available users:");
        for (String name : users.keySet()) {
            if (!name.equals(currentUser.getName())) {
                System.out.println("- " + name);
            }
        }
        
        System.out.print("Enter recipient name: ");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String recipientName = scanner.nextLine().trim();
        
        // Validate recipient exists and is not current user
        if (!users.containsKey(recipientName)) {
            System.out.println("User not found!");
            return;
        }
        
        if (recipientName.equals(currentUser.getName())) {
            System.out.println("Cannot send money to yourself!");
            return;
        }
        
        User recipient = users.get(recipientName);
        
        // Show current savings balance
        System.out.println("Your current savings balance: $" + currentUser.getSavingsAccount().getBalance());
        System.out.print("Enter amount to send: $");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            // Validate amount is positive
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            
            // Check if current user has sufficient funds in savings
            if (currentUser.getSavingsAccount().getBalance().compareTo(amount) < 0) {
                System.out.println("Insufficient funds in savings account.");
                return;
            }
            
            // Perform the transfer
            try {
                currentUser.getSavingsAccount().withdraw(amount);
                recipient.getSavingsAccount().deposit(amount);
                
                System.out.println("Successfully sent $" + amount + " to " + recipientName + ".");
                System.out.println("Your new savings balance: $" + currentUser.getSavingsAccount().getBalance());
                
            } catch (InvalidAmountException e) {
                System.out.println("Transfer failed: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Menu Option 6: Transfer Between Accounts
     * Transfers money between user's savings and investment accounts
     */
    private void transferBetweenAccounts() {
        System.out.println("\n=== Transfer Between Accounts ===");
        System.out.println("1. Transfer from Savings to Investment");
        System.out.println("2. Transfer from Investment to Savings");
        System.out.print("Select transfer direction (1 or 2): ");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String choice = scanner.nextLine().trim();
        
        boolean fromSavingsToInvestment;
        BigDecimal sourceBalance;
        String sourceAccount;
        String destinationAccount;
        
        if ("1".equals(choice)) {
            fromSavingsToInvestment = true;
            sourceBalance = currentUser.getSavingsAccount().getBalance();
            sourceAccount = "Savings";
            destinationAccount = "Investment";
        } else if ("2".equals(choice)) {
            fromSavingsToInvestment = false;
            sourceBalance = currentUser.getInvestmentAccount().getBalance();
            sourceAccount = "Investment";
            destinationAccount = "Savings";
        } else {
            System.out.println("Invalid choice. Please select 1 or 2.");
            return;
        }
        
        // Show source account balance
        System.out.println("Current " + sourceAccount + " account balance: $" + sourceBalance);
        System.out.print("Enter amount to transfer: $");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            // Validate amount is positive
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            
            // Check if source account has sufficient funds
            if (sourceBalance.compareTo(amount) < 0) {
                System.out.println("Insufficient funds in " + sourceAccount + " account.");
                return;
            }
            
            // Perform the transfer
            try {
                if (fromSavingsToInvestment) {
                    currentUser.getSavingsAccount().withdraw(amount);
                    currentUser.getInvestmentAccount().deposit(amount);
                } else {
                    currentUser.getInvestmentAccount().withdraw(amount);
                    currentUser.getSavingsAccount().deposit(amount);
                }
                
                System.out.println("Successfully transferred $" + amount + " from " + sourceAccount + " to " + destinationAccount + ".");
                System.out.println("New " + sourceAccount + " balance: $" + 
                    (fromSavingsToInvestment ? currentUser.getSavingsAccount().getBalance() : currentUser.getInvestmentAccount().getBalance()));
                System.out.println("New " + destinationAccount + " balance: $" + 
                    (fromSavingsToInvestment ? currentUser.getInvestmentAccount().getBalance() : currentUser.getSavingsAccount().getBalance()));
                
            } catch (InvalidAmountException e) {
                System.out.println("Transfer failed: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Menu Option 5: Invest in Funds
     * Allows user to invest money from investment account into specific funds
     */
    private void investInFunds() {
        System.out.println("\n=== Invest in Funds ===");
        System.out.println("Investment account balance: $" + currentUser.getInvestmentAccount().getBalance());
        
        // Show available funds
        System.out.println("\nAvailable funds:");
        System.out.println("1. LOW_RISK (2%)");
        System.out.println("2. MEDIUM_RISK (5%)");
        System.out.println("3. HIGH_RISK (10%)");
        
        System.out.print("Select fund (1-3): ");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String fundChoice = scanner.nextLine().trim();
        
        Fund selectedFund;
        switch (fundChoice) {
            case "1":
                selectedFund = Fund.LOW_RISK;
                break;
            case "2":
                selectedFund = Fund.MEDIUM_RISK;
                break;
            case "3":
                selectedFund = Fund.HIGH_RISK;
                break;
            default:
                System.out.println("Invalid fund selection. Please choose 1, 2, or 3.");
                return;
        }
        
        System.out.println("Selected: " + selectedFund);
        System.out.print("Enter amount to invest: $");
        
        // Handle EOF gracefully
        if (!scanner.hasNextLine()) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            // Validate amount is positive
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            
            // Check if user has sufficient funds in investment account
            if (currentUser.getInvestmentAccount().getBalance().compareTo(amount) < 0) {
                System.out.println("Insufficient funds in investment account.");
                return;
            }
            
            // Perform the investment
            boolean success = currentUser.getInvestmentAccount().investInFund(selectedFund, amount);
            
            if (success) {
                System.out.println("Successfully invested $" + amount + " in " + selectedFund + ".");
                System.out.println("New investment account balance: $" + currentUser.getInvestmentAccount().getBalance());
                System.out.println("Amount in " + selectedFund + ": $" + 
                    currentUser.getInvestmentAccount().getInvestmentInFund(selectedFund));
            } else {
                System.out.println("Investment failed. Please try again.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Menu Option 7: Withdraw All Investments
     * Calculates total fund value with appreciation and moves all back to investment account
     */
    private void withdrawAllInvestments() {
        System.out.println("\n=== Withdraw All Investments ===");
        
        // Calculate appreciation on all funds first
        currentUser.getInvestmentAccount().calculateInterest();
        
        // Show current fund breakdown
        System.out.println("Current fund investments:");
        BigDecimal totalFundValue = BigDecimal.ZERO;
        
        for (Fund fund : Fund.values()) {
            BigDecimal fundAmount = currentUser.getInvestmentAccount().getInvestmentInFund(fund);
            System.out.println("  " + fund + ": $" + fundAmount);
            totalFundValue = totalFundValue.add(fundAmount);
        }
        
        System.out.println("Total fund value: $" + totalFundValue);
        
        if (totalFundValue.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("No investments to withdraw.");
            return;
        }
        
        // Withdraw all investments
        BigDecimal amountWithdrawn = currentUser.getInvestmentAccount().withdrawAllInvestments();
        
        System.out.println("\nSuccessfully withdrew all investments!");
        System.out.println("Amount returned to investment account: $" + amountWithdrawn);
        System.out.println("New investment account balance: $" + currentUser.getInvestmentAccount().getBalance());
        
        // Verify funds are cleared
        System.out.println("\nFund balances after withdrawal:");
        for (Fund fund : Fund.values()) {
            BigDecimal fundAmount = currentUser.getInvestmentAccount().getInvestmentInFund(fund);
            System.out.println("  " + fund + ": $" + fundAmount);
        }
    }
    
    /**
     * Handle user logout
     */
    private void logout() {
        System.out.println("Goodbye, " + currentUser.getName() + "!");
        currentUser = null;
        isLoggedIn = false;
    }
}