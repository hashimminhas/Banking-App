# Green Day Bank - Start Frontend only

Write-Host "Starting Frontend on http://localhost:5173..." -ForegroundColor Green
Write-Host "Make sure API server is running on port 7070" -ForegroundColor Yellow
Write-Host ""

Set-Location $PSScriptRoot\..
Set-Location frontend

npm run dev
