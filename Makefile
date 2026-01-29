.PHONY: dev api ui help

help:
	@echo "Green Day Bank - Development Commands"
	@echo ""
	@echo "Usage:"
	@echo "  make dev     Start API server + frontend together"
	@echo "  make api     Start only API server (port 7070)"
	@echo "  make ui      Start only frontend (port 5173)"
	@echo "  make help    Show this help message"
	@echo ""
	@echo "Prerequisites:"
	@echo "  - Java 11+ and Gradle for API server"
	@echo "  - Node.js 16+ and npm for frontend"
	@echo ""
	@echo "First time setup:"
	@echo "  cd frontend && npm install"

dev:
	@echo "Starting Green Day Bank (API + Frontend)..."
	@echo "API Server: http://localhost:7070"
	@echo "Frontend:   http://localhost:5173"
	@echo ""
	@echo "Press Ctrl+C to stop all services"
	@cd api-server && ./gradlew run & cd frontend && npm run dev

api:
	@echo "Starting API Server on http://localhost:7070..."
	@cd api-server && ./gradlew run

ui:
	@echo "Starting Frontend on http://localhost:5173..."
	@echo "Make sure API server is running on port 7070"
	@cd frontend && npm run dev
