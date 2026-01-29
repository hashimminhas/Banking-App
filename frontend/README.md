# Green Day Bank Frontend

Modern web interface for the Green Day Bank API, built with Vite and vanilla JavaScript.

## Features

- **User Authentication:** Login with 4 pre-configured users (Alice, Bob, Charlie, Diana)
- **Real-time Balance Display:** View cash, savings, and investment balances with fund breakdown
- **Banking Operations:**
  - Deposit to savings
  - Withdraw from savings
  - Send money to other users
  - Transfer between savings and investment accounts
  - Invest in funds (Low/Medium/High risk)
  - Withdraw all investments
- **Activity Log:** Track last 20 transactions and operations
- **Toast Notifications:** Real-time success/error feedback
- **Responsive Design:** Works on desktop and mobile devices

## Prerequisites

- Node.js 16+ and npm
- Java API server running on `http://localhost:7070` (see `../api-server/`)

## Quick Start

### Installation

```bash
# Install dependencies
npm install
```

### Development

```bash
# Start development server (with hot reload)
npm run dev
```

The app will be available at `http://localhost:5173` (or the next available port).

The Vite dev server automatically proxies `/api` requests to `http://localhost:7070`.

### Production Build

```bash
# Build for production
npm run build

# Preview production build
npm run preview
```

## Project Structure

```
frontend/
├── index.html          # Main HTML file
├── main.js             # Application logic and event handlers
├── api.js              # API helper functions (fetch wrappers)
├── style.css           # Styling (plain CSS)
├── vite.config.js      # Vite configuration with proxy
├── package.json        # Dependencies and scripts
└── README.md           # This file
```

## API Integration

The frontend communicates with the Java API server via these endpoints:

- `GET /api/users` - Get list of users
- `POST /api/balance` - Get user balance (applies interest)
- `POST /api/deposit` - Deposit to savings
- `POST /api/withdraw` - Withdraw from savings
- `POST /api/send` - Send money to another user
- `POST /api/transfer` - Transfer between accounts
- `POST /api/invest` - Invest in a fund
- `POST /api/withdraw-investments` - Withdraw all investments

All API calls use the helper functions in `api.js`:
- `getJson(path)` - For GET requests
- `postJson(path, body)` - For POST requests with JSON body

## How to Use

1. **Start the API server** (in `../api-server/`):
   ```bash
   cd ../api-server
   ./gradlew run
   ```

2. **Start the frontend** (in this directory):
   ```bash
   npm run dev
   ```

3. **Login:**
   - Open `http://localhost:5173`
   - Select a user from the dropdown (Alice, Bob, Charlie, or Diana)
   - Click "Login"

4. **Perform Operations:**
   - View your balances and fund holdings
   - Use the action cards to perform transactions
   - Watch the activity log for transaction history
   - Toast notifications appear for success/error feedback

5. **Refresh Balance:**
   - Click "Refresh Balance" to update all balances
   - Interest and fund appreciation are applied on each refresh

## Features Breakdown

### Balance Display
- **Cash on Hand:** Available cash (starts at $1000)
- **Savings Account:** Balance with 1% interest
- **Investment Account:** Available investment balance
- **Funds Table:** Shows holdings in LOW_RISK, MEDIUM_RISK, and HIGH_RISK funds

### Actions
- **Deposit:** Move cash to savings account
- **Withdraw:** Move savings to cash
- **Send Money:** Transfer savings to another user
- **Transfer:** Move money between savings and investment accounts
- **Invest:** Invest investment account balance in a fund
- **Withdraw All:** Liquidate all fund holdings back to investment account

### Activity Log
- Displays last 20 operations
- Color-coded by type (success/error/info)
- Timestamped entries
- Automatically scrolls to show recent activities

## Error Handling

All API errors are caught and displayed as:
- **Toast Notification:** Temporary popup at bottom-right
- **Activity Log Entry:** Permanent record with error message

Common errors:
- **Insufficient funds:** Not enough balance for operation
- **User not found:** Invalid recipient for send money
- **Invalid amount:** Amount must be positive
- **Connection error:** API server not running

## Customization

### Styling
All styles are in `style.css` using plain CSS. Key color scheme:
- Primary: `#667eea` (purple-blue)
- Success: `#28a745` (green)
- Error: `#dc3545` (red)
- Info: `#17a2b8` (teal)

### API Configuration
To change the API server URL, edit `vite.config.js`:
```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:7070', // Change this
      changeOrigin: true
    }
  }
}
```

## Development Notes

- **No Framework:** Pure vanilla JavaScript, no React/Vue/Angular
- **ES Modules:** Uses native ES6 module imports
- **Modern JavaScript:** Async/await, arrow functions, destructuring
- **Responsive:** Flexbox and CSS Grid for layouts
- **Accessible:** Semantic HTML and proper form handling

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Troubleshooting

### API Connection Issues
- Ensure the Java API server is running on port 7070
- Check browser console for CORS errors
- Verify Vite proxy configuration in `vite.config.js`

### Build Issues
- Delete `node_modules` and run `npm install` again
- Clear Vite cache: `npm run dev -- --force`

### Hot Reload Not Working
- Check if the file is saved properly
- Restart dev server: `Ctrl+C` then `npm run dev`

## License

Part of the Kood/Jõhvi Green Day Bank project.
