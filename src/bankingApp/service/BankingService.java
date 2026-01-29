package bankingApp.service;

import bankingApp.exception.InvalidAmountException;
import bankingApp.model.Fund;
import bankingApp.model.User;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Banking Service handles all CLI interactions and banking logic
 */
public class BankingService {
    
    private final Scanner scanner;
    private final Map<String, User> users;
    private User currentUser;
    
    public BankingService(Scanner scanner) {
        this.scanner = scanner;
        this.users = new LinkedHashMap<>();
        this.currentUser = null;
        initializeUsers();
    }
    
    private String formatCurrency(BigDecimal amount) {
        return String.format("%.2f", amount);
    }
    
    private void initializeUsers() {
        users.put("Alice", new User("Alice"));
        users.put("Bob", new User("Bob"));
        users.put("Charlie", new User("Charlie"));
        users.put("Diana", new User("Diana"));
    }
    
    public void run() {
        boolean running = true;
        
        while (running) {
            if (!login()) {
                break;
            }
            
            boolean loggedIn = true;
            while (loggedIn) {
                displayMenu();
                
                System.out.print("Enter your choice: ");
                System.out.flush();
                
                if (!scanner.hasNextLine()) {
                    running = false;
                    break;
                }
                
                String input = scanner.nextLine().trim();
                
                try {
                    int choice = Integer.parseInt(input);
                    
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
                            loggedIn = false;
                            System.out.println("You have been logged out.");
                            currentUser = null;
                            break;
                        case 9:
                            loggedIn = false;
                            running = false;
                            System.out.println("Thank you for using our banking app. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
    
    private boolean login() {
        while (true) {
            System.out.print("Enter your name to login: ");
            System.out.flush();
            
            if (!scanner.hasNextLine()) {
                return false;
            }
            
            String username = scanner.nextLine().trim();
            
            if (users.containsKey(username)) {
                currentUser = users.get(username);
                System.out.println("Welcome, " + username + "!");
                return true;
            } else {
                System.out.println("User not found. Please try again.");
            }
        }
    }
    
    private void displayMenu() {
        System.out.println();
        System.out.println("--- Banking App Menu ---");
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
    
    private void showBalance() {
        currentUser.getSavingsAccount().calculateInterest();
        currentUser.getInvestmentAccount().calculateInterest();
        
        System.out.println("Savings account balance: $" + formatCurrency(currentUser.getSavingsAccount().getBalance()));
        System.out.println("Investment account balance:");
        System.out.println("* Not Invested: $" + formatCurrency(currentUser.getInvestmentAccount().getBalance()));
        
        // Display individual fund investments
        for (Fund fund : Fund.values()) {
            BigDecimal fundAmount = currentUser.getInvestmentAccount().getInvestmentInFund(fund);
            if (fundAmount.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("* " + fund.name() + ": $" + formatCurrency(fundAmount));
            }
        }
    }
    
    private void depositMoney() {
        System.out.print("Enter amount to deposit to savings account: $");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Deposit failed: amount must be positive");
                return;
            }
            
            if (currentUser.getCash().compareTo(amount) < 0) {
                System.out.println("Deposit failed: Insufficient cash on hand");
                return;
            }
            
            currentUser.depositCashToSavings(amount);
            System.out.println("Deposit successful.");
            
        } catch (NumberFormatException e) {
            // Invalid input - just return to menu
        }
    }
    
    private void withdrawMoney() {
        System.out.print("Enter amount to withdraw from savings account: $");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            currentUser.getSavingsAccount().withdraw(amount);
            System.out.println("Withdrawal successful.");
            
        } catch (InvalidAmountException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Invalid input - just return to menu
        }
    }
    
    private void sendMoney() {
        // Show available recipients (other users)
        System.out.println("Available recipients:");
        for (String name : users.keySet()) {
            if (!name.equals(currentUser.getName())) {
                System.out.println(name);
            }
        }
        
        System.out.print("Enter recipient's name: ");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String recipientName = scanner.nextLine().trim();
        
        // Validate recipient
        if (!users.containsKey(recipientName) || recipientName.equals(currentUser.getName())) {
            System.out.println("Invalid recipient.");
            return;
        }
        
        User recipient = users.get(recipientName);
        
        System.out.print("Enter amount to send: $");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Failed to send money: amount must be positive");
                return;
            }
            
            if (currentUser.getSavingsAccount().getBalance().compareTo(amount) < 0) {
                System.out.println("Failed to send money: Insufficient funds");
                return;
            }
            
            currentUser.getSavingsAccount().withdraw(amount);
            recipient.getSavingsAccount().deposit(amount);
            System.out.println("Sent $" + amount.intValue() + " to " + recipientName);
            
        } catch (InvalidAmountException e) {
            System.out.println("Failed to send money: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Invalid input - just return to menu
        }
    }
    
    private void transferBetweenAccounts() {
        System.out.println("1. Transfer from savings to investment");
        System.out.println("2. Transfer from investment to savings");
        System.out.print("Enter your choice: ");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String choice = scanner.nextLine().trim();
        
        System.out.print("Enter amount to transfer: $");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        if (!choice.equals("1") && !choice.equals("2")) {
            System.out.println("Invalid choice.");
            return;
        }
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Transfer failed: amount must be positive");
                return;
            }
            
            if (choice.equals("1")) {
                // Savings to investment
                if (currentUser.getSavingsAccount().getBalance().compareTo(amount) < 0) {
                    System.out.println("Transfer failed: Insufficient funds");
                    return;
                }
                currentUser.getSavingsAccount().withdraw(amount);
                currentUser.getInvestmentAccount().deposit(amount);
                System.out.println("Successfully transferred $" + amount.intValue() + " to investment account.");
            } else {
                // Investment to savings
                if (currentUser.getInvestmentAccount().getBalance().compareTo(amount) < 0) {
                    System.out.println("Transfer failed: Insufficient funds");
                    return;
                }
                currentUser.getInvestmentAccount().withdraw(amount);
                currentUser.getSavingsAccount().deposit(amount);
                System.out.println("Successfully transferred $" + amount.intValue() + " to savings account.");
            }
            
        } catch (InvalidAmountException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Invalid input - just return to menu
        }
    }
    
    private void investInFunds() {
        System.out.println("Available funds:");
        System.out.println("LOW_RISK");
        System.out.println("MEDIUM_RISK");
        System.out.println("HIGH_RISK");
        
        System.out.print("Enter fund to invest in: ");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String fundName = scanner.nextLine().trim();
        
        Fund selectedFund;
        try {
            selectedFund = Fund.valueOf(fundName);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid fund.");
            return;
        }
        
        System.out.print("Enter amount to invest: $");
        System.out.flush();
        
        if (!scanner.hasNextLine()) {
            return;
        }
        
        String input = scanner.nextLine().trim();
        
        try {
            BigDecimal amount = new BigDecimal(input);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Failed to invest: amount must be positive");
                return;
            }
            
            if (currentUser.getInvestmentAccount().getBalance().compareTo(amount) < 0) {
                System.out.println("Failed to invest: Insufficient funds");
                return;
            }
            
            currentUser.getInvestmentAccount().investInFund(selectedFund, amount);
            System.out.println("Successfully invested $" + amount.intValue() + " in " + fundName + " fund");
            
        } catch (NumberFormatException e) {
            // Invalid input - just return to menu
        }
    }
    
    private void withdrawAllInvestments() {
        currentUser.getInvestmentAccount().withdrawAllInvestments();
        System.out.println("All investments have been withdrawn and added to your investment account balance.");
    }
}
