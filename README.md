# Green Day Bank

Command-line banking application with savings, investments, and transfers.

## Features
- 4 user accounts (Alice, Bob, Charlie, Diana)
- Savings account with 1% interest
- Investment account with fund management
- 3 fund types (LOW_RISK 2%, MEDIUM_RISK 5%, HIGH_RISK 10%)
- Transfer money between users
- Transfer between savings and investment accounts
- Interest calculation on balance viewing
- Custom exception handling
- Input validation

## How to Run

### Compilation
```bash
javac -d out src/bankingApp/*.java src/bankingApp/model/*.java src/bankingApp/service/*.java src/bankingApp/exception/*.java
```

### Execution
```bash
java -cp out BankingApp
```

## Users
All users start with $1000 cash:
- Alice
- Bob
- Charlie
- Diana

## Menu Options
1. **Show balance** - Display savings and investment balances with interest
2. **Deposit money** - Add cash to savings account
3. **Withdraw money** - Withdraw from savings to cash
4. **Send money to a person** - Transfer cash to another user
5. **Invest in funds** - Move investment account balance to funds
6. **Transfer between accounts** - Move money between savings and investment
7. **Withdraw all investments** - Convert all fund holdings back to investment balance
8. **Logout** - Return to login screen
9. **Exit** - Quit application

## Technical Details
- **Language:** Java 8+
- **Precision:** BigDecimal for all monetary calculations
- **Architecture:** Object-oriented with inheritance and polymorphism
- **Abstract Classes:** Account base class with SavingsAccount and InvestmentAccount implementations
- **Custom Exceptions:** InvalidAmountException for validation
- **Input Handling:** Single Scanner instance, EOF-safe
- **Interest Calculation:** Applied dynamically when viewing balance

## Project Structure
```
src/bankingApp/
├── BankingApp.java                 # Main entry point
├── service/
│   └── BankingService.java         # CLI logic and menu handling
├── model/
│   ├── Account.java                # Abstract account base class
│   ├── SavingsAccount.java         # Savings with 1% interest
│   ├── InvestmentAccount.java      # Investment with fund management
│   ├── Fund.java                   # Enum for fund types
│   └── User.java                   # User with cash and accounts
└── exception/
    └── InvalidAmountException.java # Custom validation exception
```

## Development Phases
- **Phase 1:** Project structure and basic setup
- **Phase 2:** Account models and inheritance
- **Phase 3:** Exception handling implementation
- **Phase 4:** Core banking operations (deposit/withdraw)
- **Phase 5:** Transfer operations between users and accounts
- **Phase 6:** Investment system with funds
- **Phase 7:** Session management (login/logout)
- **Phase 8:** Test system compatibility and bug fixes

## Validation Rules
- Amounts must be positive
- Sufficient funds required for withdrawals
- Recipient must exist for transfers
- Investment account balance required for fund investments
- All inputs validated with clear error messages

## Notes
- No package declarations (for test system compatibility)
- No static methods except main()
- No System.exit() calls
- Currency formatted to 2 decimal places
- Interest applied on balance viewing only, not on withdrawal

## License
This project is part of the Kood/Jõhvi curriculum.
