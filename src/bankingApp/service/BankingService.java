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
                System.out.println("Send money to a person - Coming soon!");
                break;
            case 5:
                System.out.println("Invest in funds - Coming soon!");
                break;
            case 6:
                System.out.println("Transfer between accounts - Coming soon!");
                break;
            case 7:
                System.out.println("Withdraw all investments - Coming soon!");
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
     * Handle user logout
     */
    private void logout() {
        System.out.println("Goodbye, " + currentUser.getName() + "!");
        currentUser = null;
        isLoggedIn = false;
    }
}