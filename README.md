# Green Day Bank

Full-stack banking application with CLI, REST API, and web interface.

## 🚀 Quick Start

### Run Everything (API + Frontend)
```bash
# On Windows
.\scripts\dev.ps1

# On Unix/Linux/macOS
make dev
```

This starts:
- API Server on `http://localhost:7070`
- Web Frontend on `http://localhost:5173`

---

## 📦 Project Structure

This repository contains three independent applications:

1. **CLI App** (`src/`) - Command-line banking interface (for automated testing)
2. **API Server** (`api-server/`) - REST API with Javalin
3. **Web Frontend** (`frontend/`) - Modern web interface with Vite

---

## 🖥️ CLI Application

Command-line banking application for automated testing.

### Features
- 4 user accounts (Alice, Bob, Charlie, Diana)
- Savings account with 1% interest
- Investment account with fund management
- 3 fund types (LOW_RISK 2%, MEDIUM_RISK 5%, HIGH_RISK 10%)
- Transfer money between users
- Transfer between savings and investment accounts
- Interest calculation on balance viewing
- Custom exception handling
- Input validation

### How to Run

**Compilation:**
```bash
javac -d out src/bankingApp/*.java src/bankingApp/model/*.java src/bankingApp/service/*.java src/bankingApp/exception/*.java
```

**Execution:**
```bash
java -cp out BankingApp
```

### Menu Options
1. **Show balance** - Display savings and investment balances with interest
2. **Deposit money** - Add cash to savings account
3. **Withdraw money** - Withdraw from savings to cash
4. **Send money to a person** - Transfer cash to another user
5. **Invest in funds** - Move investment account balance to funds
6. **Transfer between accounts** - Move money between savings and investment
7. **Withdraw all investments** - Convert all fund holdings back to investment balance
8. **Logout** - Return to login screen
9. **Exit** - Quit application

---

## 🌐 API Server

REST API built with Javalin, exposing banking operations as JSON endpoints.

### Prerequisites
- Java 11+
- Gradle (wrapper included)

### How to Run

```bash
# Using Gradle wrapper
cd api-server
./gradlew run

# Or on Windows
cd api-server
gradlew.bat run

# Or using make
make api
```

**Server runs on:** `http://localhost:7070`

### API Endpoints

All endpoints are prefixed with `/api`:

- `GET /api/health` - Health check
- `GET /api/users` - List all users
- `POST /api/balance` - Get balance (applies interest)
- `POST /api/deposit` - Deposit to savings
- `POST /api/withdraw` - Withdraw from savings
- `POST /api/send` - Send money to another user
- `POST /api/transfer` - Transfer between accounts
- `POST /api/invest` - Invest in a fund
- `POST /api/withdraw-investments` - Withdraw all investments

**Documentation:** See [api-server/README.md](api-server/README.md)

### Features
- CORS enabled for `localhost:5173`
- JSON error responses
- BigDecimal precision
- In-memory state (persists while running)

---

## 🎨 Web Frontend

Modern single-page application built with Vite and vanilla JavaScript.

### Prerequisites
- Node.js 16+
- npm

### How to Run

```bash
# Install dependencies (first time only)
cd frontend
npm install

# Start development server
npm run dev

# Or using make
make ui
```

**Frontend runs on:** `http://localhost:5173`

### Features
- User selection and login
- Real-time balance display
- All banking operations (deposit, withdraw, send, transfer, invest)
- Activity log (last 20 operations)
- Toast notifications
- Responsive design
- Vite proxy for API calls (no CORS issues)

**Documentation:** See [frontend/README.md](frontend/README.md)

---

## 🛠️ Development Commands

### Using Make (Unix/Linux/macOS)

```bash
make dev     # Start API server + frontend
make api     # Start only API server
make ui      # Start only frontend
```

### Using PowerShell (Windows)

```powershell
.\scripts\dev.ps1      # Start API server + frontend
.\scripts\api.ps1      # Start only API server
.\scripts\ui.ps1       # Start only frontend
```

---

## 🔧 Configuration

### Ports
- **CLI:** N/A (command-line only)
- **API Server:** `7070` (configurable via `PORT` environment variable)
- **Frontend:** `5173` (Vite default)

### API Proxy
The frontend uses Vite's proxy feature to forward `/api` requests to `http://localhost:7070`, avoiding CORS issues during development.

---

## 👥 Users

## 👥 Users

All users start with $1000 cash:
- Alice
- Bob
- Charlie
- Diana

---

## 📋 Technical Details

### CLI Application
- **Language:** Java 8+
- **Precision:** BigDecimal for all monetary calculations
- **Architecture:** Object-oriented with inheritance and polymorphism
- **Abstract Classes:** Account base class with SavingsAccount and InvestmentAccount implementations
- **Custom Exceptions:** InvalidAmountException for validation
- **Input Handling:** Single Scanner instance, EOF-safe
- **Interest Calculation:** Applied dynamically when viewing balance
- **No package declarations** (for test system compatibility)
- **No static methods** except main()
- **No System.exit()** calls

### API Server
- **Framework:** Javalin 5.6.3
- **JSON:** Jackson for serialization
- **Logging:** SLF4J Simple Logger
- **Architecture:** Clean separation (Controller → Service → Model)
- **Precision:** BigDecimal internally, 2 decimal places in responses
- **State:** In-memory (persists while running)
- **CORS:** Enabled for `localhost:5173` and `localhost:3000`

### Frontend
- **Build Tool:** Vite 5.0.8
- **Framework:** Vanilla JavaScript (no React/Vue/Angular)
- **Modules:** ES6 modules with async/await
- **Styling:** Plain CSS with flexbox and grid
- **Proxy:** Vite dev server proxies `/api` to `localhost:7070`
- **Browser Support:** Chrome 90+, Firefox 88+, Safari 14+, Edge 90+

---

## 📂 Project Structure

```
Banking-App/
├── src/bankingApp/                 # CLI Application
│   ├── BankingApp.java            # Main entry point
│   ├── service/
│   │   └── BankingService.java    # CLI logic and menu handling
│   ├── model/
│   │   ├── Account.java           # Abstract account base class
│   │   ├── SavingsAccount.java    # Savings with 1% interest
│   │   ├── InvestmentAccount.java # Investment with fund management
│   │   ├── Fund.java              # Enum for fund types
│   │   └── User.java              # User with cash and accounts
│   └── exception/
│       └── InvalidAmountException.java # Custom validation exception
│
├── api-server/                     # REST API Server
│   ├── build.gradle               # Gradle configuration
│   ├── src/main/java/com/greendaybank/
│   │   ├── ApiServer.java         # Main entry point
│   │   ├── controller/            # API endpoints
│   │   ├── service/               # Business logic
│   │   ├── model/                 # Domain models
│   │   └── dto/                   # Request/response DTOs
│   └── README.md                  # API documentation
│
├── frontend/                       # Web Frontend
│   ├── index.html                 # Main HTML
│   ├── main.js                    # Application logic
│   ├── api.js                     # API helpers
│   ├── style.css                  # Styling
│   ├── vite.config.js             # Vite configuration
│   ├── package.json               # Dependencies
│   └── README.md                  # Frontend documentation
│
├── docs/                           # Development documentation
│   ├── Phase1-Report.md           # Project structure
│   ├── Phase2-Report.md           # Account models
│   ├── Phase3-Report.md           # Exception handling
│   ├── Phase4-Report.md           # Core operations
│   ├── Phase5-Report.md           # Transfer operations
│   ├── Phase6-Report.md           # Investment system
│   ├── Phase7-Report.md           # Session management
│   └── Phase8-Report.md           # Test compatibility
│
├── scripts/                        # Helper scripts
│   ├── dev.ps1                    # Start all (Windows)
│   ├── api.ps1                    # Start API (Windows)
│   └── ui.ps1                     # Start UI (Windows)
│
├── Makefile                        # Development commands (Unix/Linux/macOS)
└── README.md                       # This file
```

---

## 🧪 Development Phases

- **Phase 1:** Project structure and basic setup
- **Phase 2:** Account models and inheritance
- **Phase 3:** Exception handling implementation
- **Phase 4:** Core banking operations (deposit/withdraw)
- **Phase 5:** Transfer operations between users and accounts
- **Phase 6:** Investment system with funds
- **Phase 7:** Session management (login/logout)
- **Phase 8:** Test system compatibility and bug fixes

---

## ✅ Validation Rules

- Amounts must be positive
- Sufficient funds required for withdrawals
- Recipient must exist for transfers
- Investment account balance required for fund investments
- All inputs validated with clear error messages

---

## 📝 License

This project is part of the Kood/Jõhvi curriculum.
