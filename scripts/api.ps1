# Green Day Bank - Start API Server only

Write-Host "Starting API Server on http://localhost:7070..." -ForegroundColor Green
Write-Host ""

Set-Location $PSScriptRoot\..
Set-Location api-server

if ($IsWindows -or $env:OS -match "Windows") {
    .\gradlew.bat run
} else {
    ./gradlew run
}
