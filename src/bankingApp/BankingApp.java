package bankingApp;

import bankingApp.service.BankingService;
import java.util.Scanner;

/**
 * Main entry point for the Green Day Banking Application
 * Phase 1: Basic project setup with login system and menu display
 */
public class BankingApp {
    
    public static void main(String[] args) {
        // Create single Scanner instance for entire application lifecycle
        try (Scanner scanner = new Scanner(System.in)) {
            // Create banking service instance and pass scanner
            BankingService bankingService = new BankingService(scanner);
            
            // Start the banking application
            bankingService.run();
        }
    }
}