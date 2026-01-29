package bankingApp.exception;

/**
 * Custom exception for invalid amount operations
 * Phase 1: Basic structure setup for future phases
 */
public class InvalidAmountException extends Exception {
    
    public InvalidAmountException(String message) {
        super(message);
    }
    
    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}