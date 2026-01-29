import bankingApp.service.BankingService;
import java.util.Scanner;

/**
 * Main entry point for the Green Day Banking Application
 */
public class Main {
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BankingService bankingService = new BankingService(scanner);
            bankingService.run();
        }
    }
}
