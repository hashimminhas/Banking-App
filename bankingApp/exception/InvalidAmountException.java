package bankingApp.exception;

/**
 * Custom exception for invalid amount operations
 * Phase 3: Exception handling for deposit/withdrawal validation
 * 
 * Thrown when:
 * - Amount is negative or zero
 * - Insufficient funds for withdrawal
 * - Invalid monetary operation
 */
public class InvalidAmountException extends Exception {
    
    public InvalidAmountException(String message) {
        super(message);
    }
    
    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}