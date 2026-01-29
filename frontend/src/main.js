import { getJson, postJson } from './api.js';

// App state
let currentUser = null;
let activityLog = [];

// DOM elements
const loginSection = document.getElementById('loginSection');
const dashboardSection = document.getElementById('dashboardSection');
const userSelect = document.getElementById('userSelect');
const loginBtn = document.getElementById('loginBtn');
const logoutBtn = document.getElementById('logoutBtn');
const currentUserSpan = document.getElementById('currentUser');
const refreshBalanceBtn = document.getElementById('refreshBalanceBtn');
const sendRecipient = document.getElementById('sendRecipient');
const toast = document.getElementById('toast');

// Initialize app
async function init() {
  try {
    const data = await getJson('/api/users');
    populateUserSelect(data.users);
  } catch (error) {
    showToast('Failed to load users: ' + error.message, 'error');
  }
}

// Populate user select dropdowns
function populateUserSelect(users) {
  users.forEach(user => {
    const option = document.createElement('option');
    option.value = user;
    option.textContent = user;
    userSelect.appendChild(option.cloneNode(true));
    sendRecipient.appendChild(option);
  });
}

// Login
userSelect.addEventListener('change', () => {
  loginBtn.disabled = !userSelect.value;
});

loginBtn.addEventListener('click', () => {
  currentUser = userSelect.value;
  login();
});

function login() {
  currentUserSpan.textContent = `Logged in as ${currentUser}`;
  loginSection.style.display = 'none';
  dashboardSection.style.display = 'block';
  loadBalance();
  addActivity('Logged in', 'info');
}

// Logout
logoutBtn.addEventListener('click', () => {
  currentUser = null;
  activityLog = [];
  loginSection.style.display = 'flex';
  dashboardSection.style.display = 'none';
  userSelect.value = '';
  loginBtn.disabled = true;
  clearActivityLog();
});

// Load balance
refreshBalanceBtn.addEventListener('click', loadBalance);

async function loadBalance() {
  try {
    const balance = await postJson('/api/balance', { user: currentUser });
    updateBalanceUI(balance);
    addActivity('Balance refreshed', 'info');
  } catch (error) {
    showToast('Failed to load balance: ' + error.message, 'error');
  }
}

function updateBalanceUI(balance) {
  document.getElementById('cashAmount').textContent = `$${balance.cash.toFixed(2)}`;
  document.getElementById('savingsAmount').textContent = `$${balance.savingsBalance.toFixed(2)}`;
  document.getElementById('investmentAmount').textContent = `$${balance.investmentBalance.toFixed(2)}`;
  
  const fundsBody = document.getElementById('fundsTableBody');
  fundsBody.innerHTML = '';
  
  for (const [fund, amount] of Object.entries(balance.funds)) {
    const row = document.createElement('tr');
    row.innerHTML = `<td>${fund}</td><td>$${amount.toFixed(2)}</td>`;
    fundsBody.appendChild(row);
  }
}

// Deposit
document.getElementById('depositForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const amount = parseFloat(document.getElementById('depositAmount').value);
  
  if (amount <= 0) {
    showToast('Amount must be positive', 'error');
    return;
  }
  
  try {
    await postJson('/api/deposit', { user: currentUser, amount });
    showToast(`Deposited $${amount.toFixed(2)} to savings`, 'success');
    addActivity(`Deposited $${amount.toFixed(2)} to savings`, 'success');
    document.getElementById('depositForm').reset();
    loadBalance();
  } catch (error) {
    showToast('Deposit failed: ' + error.message, 'error');
    addActivity(`Deposit failed: ${error.message}`, 'error');
  }
});

// Withdraw
document.getElementById('withdrawForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const amount = parseFloat(document.getElementById('withdrawAmount').value);
  
  if (amount <= 0) {
    showToast('Amount must be positive', 'error');
    return;
  }
  
  try {
    await postJson('/api/withdraw', { user: currentUser, amount });
    showToast(`Withdrew $${amount.toFixed(2)} from savings`, 'success');
    addActivity(`Withdrew $${amount.toFixed(2)} from savings`, 'success');
    document.getElementById('withdrawForm').reset();
    loadBalance();
  } catch (error) {
    showToast('Withdrawal failed: ' + error.message, 'error');
    addActivity(`Withdrawal failed: ${error.message}`, 'error');
  }
});

// Send money
document.getElementById('sendForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const recipient = document.getElementById('sendRecipient').value;
  const amount = parseFloat(document.getElementById('sendAmount').value);
  
  if (!recipient) {
    showToast('Please select a recipient', 'error');
    return;
  }
  
  if (amount <= 0) {
    showToast('Amount must be positive', 'error');
    return;
  }
  
  if (recipient === currentUser) {
    showToast('Cannot send money to yourself', 'error');
    return;
  }
  
  try {
    await postJson('/api/send', { from: currentUser, to: recipient, amount });
    showToast(`Sent $${amount.toFixed(2)} to ${recipient}`, 'success');
    addActivity(`Sent $${amount.toFixed(2)} to ${recipient}`, 'success');
    document.getElementById('sendForm').reset();
    loadBalance();
  } catch (error) {
    showToast('Send failed: ' + error.message, 'error');
    addActivity(`Send failed: ${error.message}`, 'error');
  }
});

// Transfer
document.getElementById('transferForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const direction = document.getElementById('transferDirection').value;
  const amount = parseFloat(document.getElementById('transferAmount').value);
  
  if (!direction) {
    showToast('Please select a direction', 'error');
    return;
  }
  
  if (amount <= 0) {
    showToast('Amount must be positive', 'error');
    return;
  }
  
  try {
    await postJson('/api/transfer', { user: currentUser, direction, amount });
    const directionText = direction === 'SAVINGS_TO_INVESTMENT' 
      ? 'Savings → Investment' 
      : 'Investment → Savings';
    showToast(`Transferred $${amount.toFixed(2)} (${directionText})`, 'success');
    addActivity(`Transferred $${amount.toFixed(2)} (${directionText})`, 'success');
    document.getElementById('transferForm').reset();
    loadBalance();
  } catch (error) {
    showToast('Transfer failed: ' + error.message, 'error');
    addActivity(`Transfer failed: ${error.message}`, 'error');
  }
});

// Invest
document.getElementById('investForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const fund = document.getElementById('investFund').value;
  const amount = parseFloat(document.getElementById('investAmount').value);
  
  if (!fund) {
    showToast('Please select a fund', 'error');
    return;
  }
  
  if (amount <= 0) {
    showToast('Amount must be positive', 'error');
    return;
  }
  
  try {
    await postJson('/api/invest', { user: currentUser, fund, amount });
    showToast(`Invested $${amount.toFixed(2)} in ${fund}`, 'success');
    addActivity(`Invested $${amount.toFixed(2)} in ${fund}`, 'success');
    document.getElementById('investForm').reset();
    loadBalance();
  } catch (error) {
    showToast('Investment failed: ' + error.message, 'error');
    addActivity(`Investment failed: ${error.message}`, 'error');
  }
});

// Withdraw all investments
document.getElementById('withdrawInvestmentsBtn').addEventListener('click', async () => {
  try {
    await postJson('/api/withdraw-investments', { user: currentUser });
    showToast('All investments withdrawn successfully', 'success');
    addActivity('Withdrew all investments', 'success');
    loadBalance();
  } catch (error) {
    showToast('Withdrawal failed: ' + error.message, 'error');
    addActivity(`Withdrawal failed: ${error.message}`, 'error');
  }
});

// Activity log
function addActivity(message, type = 'info') {
  const timestamp = new Date().toLocaleTimeString();
  activityLog.unshift({ message, type, timestamp });
  
  // Keep only last 20 activities
  if (activityLog.length > 20) {
    activityLog = activityLog.slice(0, 20);
  }
  
  updateActivityLog();
}

function updateActivityLog() {
  const logContainer = document.getElementById('activityLog');
  
  if (activityLog.length === 0) {
    logContainer.innerHTML = '<p class="log-empty">No activities yet</p>';
    return;
  }
  
  logContainer.innerHTML = activityLog.map(activity => `
    <div class="activity-item activity-${activity.type}">
      <span class="activity-time">${activity.timestamp}</span>
      <span class="activity-message">${activity.message}</span>
    </div>
  `).join('');
}

function clearActivityLog() {
  activityLog = [];
  updateActivityLog();
}

// Toast notifications
function showToast(message, type = 'info') {
  toast.textContent = message;
  toast.className = `toast toast-${type} show`;
  
  setTimeout(() => {
    toast.classList.remove('show');
  }, 3000);
}

// Initialize app on load
init();
