# Green Day Bank - Start Frontend only

Write-Host "Starting Frontend on http://localhost:5173..." -ForegroundColor Green
Write-Host "Make sure API server is running on port 7070" -ForegroundColor Yellow
Write-Host ""

Set-Location $PSScriptRoot\..
Set-Location frontend

# Check if dependencies are installed
if (-not (Test-Path "node_modules")) {
    Write-Host "Frontend dependencies not found. Installing..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to install frontend dependencies. Please run 'npm install' manually." -ForegroundColor Red
        exit 1
    }
    Write-Host "Dependencies installed successfully!" -ForegroundColor Green
    Write-Host ""
}

npm run dev
