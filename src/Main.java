import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Green Day Banking Application - Single file version for test compatibility
 */
public class Main {
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            BankingService bankingService = new BankingService(scanner);
            bankingService.run();
        }
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

enum Fund {
    LOW_RISK(new BigDecimal("0.02")),
    MEDIUM_RISK(new BigDecimal("0.05")),
    HIGH_RISK(new BigDecimal("0.10"));
    
    private final BigDecimal appreciationRate;
    
    Fund(BigDecimal appreciationRate) {
        this.appreciationRate = appreciationRate;
    }
    
    public BigDecimal getAppreciationRate() {
        return appreciationRate;
    }
}

abstract class Account {
    protected BigDecimal balance;
    
    public Account() {
        this.balance = BigDecimal.ZERO;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void deposit(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("amount must be positive");
        }
        balance = balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InvalidAmountException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
    
    public abstract void calculateInterest();
}

class SavingsAccount extends Account {
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.01");
    
    public SavingsAccount() {
        super();
    }
    
    @Override
    public void calculateInterest() {
        BigDecimal interest = balance.multiply(INTEREST_RATE);
        balance = balance.add(interest);
    }
}

class InvestmentAccount extends Account {
    private final Map<Fund, BigDecimal> investments;
    
    public InvestmentAccount() {
        super();
        this.investments = new HashMap<>();
    }
    
    public void investInFund(Fund fund, BigDecimal amount) {
        balance = balance.subtract(amount);
        BigDecimal currentInvestment = investments.getOrDefault(fund, BigDecimal.ZERO);
        investments.put(fund, currentInvestment.add(amount));
    }
    
    public void withdrawAllInvestments() {
        for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
            balance = balance.add(entry.getValue());
        }
        investments.clear();
    }
    
    @Override
    public void calculateInterest() {
        for (Map.Entry<Fund, BigDecimal> entry : investments.entrySet()) {
            Fund fund = entry.getKey();
            BigDecimal currentAmount = entry.getValue();
            BigDecimal appreciation = currentAmount.multiply(fund.getAppreciationRate());
            investments.put(fund, currentAmount.add(appreciation));
        }
    }
    
    public Map<Fund, BigDecimal> getInvestments() {
        return investments;
    }
}

class User {
    private final String name;
    private BigDecimal cash;
    private final SavingsAccount savingsAccount;
    private final InvestmentAccount investmentAccount;
    
    public User(String name) {
        this.name = name;
        this.cash = new BigDecimal("1000");
        this.savingsAccount = new SavingsAccount();
        this.investmentAccount = new InvestmentAccount();
    }
    
    public String getName() {
        return name;
    }
    
    public BigDecimal getCash() {
        return cash;
    }
    
    public SavingsAccount getSavingsAccount() {
        return savingsAccount;
    }
    
    public InvestmentAccount getInvestmentAccount() {
        return investmentAccount;
    }
    
    public void depositCashToSavings(BigDecimal amount) {
        cash = cash.subtract(amount);
        try {
            savingsAccount.deposit(amount);
        } catch (InvalidAmountException e) {
            cash = cash.add(amount);
        }
    }
    
    public void withdrawSavingsToCash(BigDecimal amount) throws InvalidAmountException {
        savingsAccount.withdraw(amount);
        cash = cash.add(amount);
    }
}

class BankingService {
    
    private final Scanner scanner;
    private final Map<String, User> users;
    private User currentUser;
    private final DecimalFormat currencyFormatter;
    
    public BankingService(Scanner scanner) {
        this.scanner = scanner;
        this.users = new LinkedHashMap<>();
        this.currentUser = null;
        this.currencyFormatter = new DecimalFormat("#0.00");
        initializeUsers();
    }
    
    private String formatCurrency(BigDecimal amount) {
        return currencyFormatter.format(amount);
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
                if (currentUser.getSavingsAccount().getBalance().compareTo(amount) < 0) {
                    System.out.println("Transfer failed: Insufficient funds");
                    return;
                }
                currentUser.getSavingsAccount().withdraw(amount);
                currentUser.getInvestmentAccount().deposit(amount);
                System.out.println("Successfully transferred $" + amount.intValue() + " to investment account.");
            } else {
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
        currentUser.getInvestmentAccount().calculateInterest();
        currentUser.getInvestmentAccount().withdrawAllInvestments();
        System.out.println("All investments have been withdrawn and added to your investment account balance.");
    }
}
