# Green Day Bank - Development Scripts (Windows PowerShell)
# Start API server + frontend together

Write-Host "Starting Green Day Bank (API + Frontend)..." -ForegroundColor Green
Write-Host "API Server: http://localhost:7070" -ForegroundColor Cyan
Write-Host "Frontend:   http://localhost:5173" -ForegroundColor Cyan
Write-Host ""

# Check if frontend dependencies are installed
Set-Location $PSScriptRoot\..
Set-Location frontend

if (-not (Test-Path "node_modules")) {
    Write-Host "Frontend dependencies not found. Installing..." -ForegroundColor Yellow
    npm install
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Failed to install frontend dependencies. Please run 'npm install' manually in the frontend directory." -ForegroundColor Red
        exit 1
    }
    Write-Host "Dependencies installed successfully!" -ForegroundColor Green
    Write-Host ""
}

Set-Location $PSScriptRoot\..

Write-Host "Press Ctrl+C to stop all services" -ForegroundColor Yellow
Write-Host ""

# Start API server in background
$apiJob = Start-Job -ScriptBlock {
    Set-Location $using:PSScriptRoot\..
    Set-Location api-server
    if ($IsWindows -or $env:OS -match "Windows") {
        .\gradlew.bat run
    } else {
        ./gradlew run
    }
}

Write-Host "API server starting (Job ID: $($apiJob.Id))..." -ForegroundColor Yellow

# Wait a bit for API server to initialize
Start-Sleep -Seconds 5

# Start frontend in foreground
Set-Location $PSScriptRoot\..
Set-Location frontend

Write-Host "Starting frontend..." -ForegroundColor Yellow
npm run dev

# Cleanup: Stop API server when frontend stops
Write-Host ""
Write-Host "Stopping API server..." -ForegroundColor Yellow
Stop-Job -Job $apiJob
Remove-Job -Job $apiJob
Write-Host "All services stopped." -ForegroundColor Green
