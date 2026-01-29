package bankingApp;

import bankingApp.service.BankingService;
import java.util.Scanner;

/**
 * Main entry point for the Green Day Banking Application
 * All banking operations are handled through BankingService
 */
public class BankingApp {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankingService bankingService = new BankingService(scanner);
        bankingService.run();
    }
}