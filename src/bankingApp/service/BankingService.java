package bankingApp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Banking Service handles all CLI interactions and banking logic
 * Phase 1: Implements login system and basic menu display
 */
public class BankingService {
    
    private final Scanner scanner;
    private final List<String> validUsers;
    private String currentUser;
    private boolean isLoggedIn;
    
    public BankingService(Scanner scanner) {
        this.scanner = scanner;
        this.validUsers = Arrays.asList("Alice", "Bob", "Charlie", "Diana");
        this.isLoggedIn = false;
        this.currentUser = null;
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
        
        if (validUsers.contains(username)) {
            currentUser = username;
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
                System.out.println("Show balance - Coming soon!");
                break;
            case 2:
                System.out.println("Deposit money - Coming soon!");
                break;
            case 3:
                System.out.println("Withdraw money - Coming soon!");
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
     * Handle user logout
     */
    private void logout() {
        System.out.println("Goodbye, " + currentUser + "!");
        currentUser = null;
        isLoggedIn = false;
    }
}