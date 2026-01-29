import java.util.Scanner;

/**
 * Main entry point for the Green Day Banking Application
 * All banking operations are handled through BankingService
 */
public class BankingApp {
    
    public static void main(String[] args) {
        System.out.flush();
        Scanner scanner = new Scanner(System.in);
        BankingService bankingService = new BankingService(scanner);
        bankingService.run();
    }
}