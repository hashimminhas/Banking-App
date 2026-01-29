import java.math.BigDecimal;

/**
 * Abstract base class for all account types
 * Phase 3: Added validation and custom exception handling
 */
public abstract class Account {
    protected BigDecimal balance;
    
    public Account() {
        this.balance = BigDecimal.ZERO;
    }
    
    public Account(BigDecimal initialBalance) {
        this.balance = initialBalance;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    protected void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    /**
     * Calculate and apply interest to the account
     */
    public abstract void calculateInterest();
    
    /**
     * Get the type of account
     * @return Account type as string
     */
    public abstract String getAccountType();
    
    /**
     * Deposit money into the account
     * @param amount Amount to deposit
     * @throws InvalidAmountException if amount is not positive
     */
    public void deposit(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("amount must be positive");
        }
        balance = balance.add(amount);
    }
    
    /**
     * Withdraw money from the account
     * @param amount Amount to withdraw
     * @throws InvalidAmountException if amount is not positive or exceeds balance
     */
    public void withdraw(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new InvalidAmountException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }
}