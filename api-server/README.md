# Green Day Bank API Server

RESTful JSON API for the Green Day Bank banking application, built with Javalin.

## Features

- **4 Users:** Alice, Bob, Charlie, Diana (each starts with $1000 cash)
- **Savings Account:** 1% interest applied when balance is requested
- **Investment Account:** Support for 3 fund types with appreciation
  - LOW_RISK: 2% appreciation
  - MEDIUM_RISK: 5% appreciation
  - HIGH_RISK: 10% appreciation
- **Operations:** Deposit, withdraw, transfer, invest, send money between users
- **CORS Enabled:** For local frontend development
- **Centralized Error Handling:** Consistent JSON error responses

## Quick Start

### Prerequisites
- Java 11 or higher
- Gradle (wrapper included)

### Build and Run

```bash
# Build the project
./gradlew build

# Run the server (starts on port 7070 by default)
./gradlew run

# Or specify a custom port
PORT=8080 ./gradlew run
```

The server will start on `http://localhost:7070` (or your specified PORT).

## API Endpoints

All endpoints are prefixed with `/api`.

### GET /api/health
Health check endpoint.

**Response:**
```json
{
  "status": "ok"
}
```

### GET /api/users
Get list of all users.

**Response:**
```json
{
  "users": ["Alice", "Bob", "Charlie", "Diana"]
}
```

### POST /api/balance
Get user balance with interest/appreciation applied.

**Request:**
```json
{
  "user": "Alice"
}
```

**Response:**
```json
{
  "user": "Alice",
  "cash": 1000.00,
  "savingsBalance": 0.00,
  "investmentBalance": 0.00,
  "funds": {
    "LOW_RISK": 0.00,
    "MEDIUM_RISK": 0.00,
    "HIGH_RISK": 0.00
  }
}
```

**Note:** This endpoint applies savings interest and fund appreciation before returning balances.

### POST /api/deposit
Deposit cash to savings account.

**Request:**
```json
{
  "user": "Alice",
  "amount": 500
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Deposit completed"
}
```

**Errors:**
- `409 INSUFFICIENT_FUNDS`: If user has insufficient cash on hand

### POST /api/withdraw
Withdraw from savings to cash.

**Request:**
```json
{
  "user": "Alice",
  "amount": 100
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Withdrawal completed"
}
```

**Errors:**
- `409 INSUFFICIENT_FUNDS`: If savings balance is insufficient

### POST /api/send
Send money from one user's savings to another user's savings.

**Request:**
```json
{
  "from": "Alice",
  "to": "Bob",
  "amount": 100
}
```

**Response:**
```json
{
  "status": "success",
  "message": "Money sent successfully"
}
```

**Errors:**
- `404 NOT_FOUND`: If sender or recipient user not found
- `400 BAD_REQUEST`: If trying to send to yourself
- `409 INSUFFICIENT_FUNDS`: If sender has insufficient savings

### POST /api/transfer
Transfer money between savings and investment accounts for the same user.

**Request:**
```json
{
  "user": "Alice",
  "direction": "SAVINGS_TO_INVESTMENT",
  "amount": 100
}
```

**Directions:**
- `SAVINGS_TO_INVESTMENT`: Transfer from savings to investment account
- `INVESTMENT_TO_SAVINGS`: Transfer from investment to savings account

**Response:**
```json
{
  "status": "success",
  "message": "Transfer completed"
}
```

**Errors:**
- `409 INSUFFICIENT_FUNDS`: If source account has insufficient balance

### POST /api/invest
Invest money from investment account balance into a specific fund.

**Request:**
```json
{
  "user": "Alice",
  "fund": "LOW_RISK",
  "amount": 100
}
```

**Fund Types:**
- `LOW_RISK`: 2% appreciation rate
- `MEDIUM_RISK`: 5% appreciation rate
- `HIGH_RISK`: 10% appreciation rate

**Response:**
```json
{
  "status": "success",
  "message": "Investment completed"
}
```

**Errors:**
- `400 BAD_REQUEST`: Invalid fund name
- `409 INSUFFICIENT_FUNDS`: If investment account balance is insufficient

### POST /api/withdraw-investments
Withdraw all fund investments back to investment account balance.

**Request:**
```json
{
  "user": "Alice"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "All investments withdrawn"
}
```

**Note:** This moves ALL fund holdings back to the investment account cash balance and sets all funds to 0.

## Error Response Format

All errors follow this consistent format:

```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Error description"
  }
}
```

**Error Codes:**
- `BAD_REQUEST` (400): Invalid request data or validation failure
- `NOT_FOUND` (404): User not found
- `INSUFFICIENT_FUNDS` (409): Not enough funds for operation
- `INTERNAL_ERROR` (500): Unexpected server error

## Technical Details

- **Framework:** Javalin 5.6.3
- **JSON Serialization:** Jackson
- **Logging:** SLF4J Simple Logger
- **Architecture:** Clean separation of concerns (Controller → Service → Model)
- **Precision:** BigDecimal for all monetary calculations internally
- **Response Format:** All monetary values returned as doubles with 2 decimal places
- **State:** In-memory persistent state while server is running (no database)
- **CORS:** Enabled for `http://localhost:5173` and `http://localhost:3000`

## Project Structure

```
api-server/
├── build.gradle                 # Gradle build configuration
├── settings.gradle              # Gradle settings
├── src/main/java/com/greendaybank/
│   ├── ApiServer.java           # Main entry point
│   ├── controller/
│   │   └── BankingController.java    # API endpoint handlers
│   ├── service/
│   │   └── BankingService.java       # Business logic
│   ├── model/
│   │   ├── Account.java              # Abstract account base
│   │   ├── SavingsAccount.java       # Savings with 1% interest
│   │   ├── InvestmentAccount.java    # Investment with funds
│   │   ├── Fund.java                 # Fund types enum
│   │   └── User.java                 # User model
│   └── dto/
│       ├── ErrorResponse.java        # Error DTO
│       ├── BalanceResponse.java      # Balance DTO
│       ├── SuccessResponse.java      # Success DTO
│       └── ... (various request DTOs)
```

## Development

### Running in Development
```bash
./gradlew run
```

### Building a JAR
```bash
./gradlew build
```

The JAR will be in `build/libs/`.

### Testing with cURL

```bash
# Health check
curl http://localhost:7070/api/health

# Get users
curl http://localhost:7070/api/users

# Get balance
curl -X POST http://localhost:7070/api/balance \
  -H "Content-Type: application/json" \
  -d '{"user":"Alice"}'

# Deposit
curl -X POST http://localhost:7070/api/deposit \
  -H "Content-Type: application/json" \
  -d '{"user":"Alice","amount":500}'

# Invest
curl -X POST http://localhost:7070/api/invest \
  -H "Content-Type: application/json" \
  -d '{"user":"Alice","fund":"LOW_RISK","amount":100}'
```

## Notes

- No `System.exit()` calls - server gracefully handles shutdown signals
- No static methods except `main()`
- Fund appreciation is applied every time balance is requested
- Savings interest is applied every time balance is requested
- All fund listings maintain enum order (LOW_RISK, MEDIUM_RISK, HIGH_RISK)
- State is in-memory and resets on server restart

## License

Part of the Kood/Jõhvi Green Day Bank project.
