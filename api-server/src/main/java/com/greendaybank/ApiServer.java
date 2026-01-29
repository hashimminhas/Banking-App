package com.greendaybank;

import com.greendaybank.controller.BankingController;
import com.greendaybank.service.BankingService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.bundled.CorsPluginConfig;

/**
 * Main API Server using Javalin
 */
public class ApiServer {
    
    public static void main(String[] args) {
        // Get port from environment or default to 7070
        int port = getPort();
        
        // Initialize service and controller
        BankingService bankingService = new BankingService();
        BankingController controller = new BankingController(bankingService);
        
        // Create Javalin app with CORS enabled
        Javalin app = Javalin.create(config -> {
            // Enable CORS for local development
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.allowHost("http://localhost:5173");
                    it.allowHost("http://localhost:3000");
                    it.allowHost("http://127.0.0.1:5173");
                    it.allowHost("http://127.0.0.1:3000");
                });
            });
        }).start(port);
        
        System.out.println("Green Day Bank API Server started on port " + port);
        
        // Define routes
        app.get("/api/users", controller::getUsers);
        app.post("/api/balance", controller::getBalance);
        app.post("/api/deposit", controller::deposit);
        app.post("/api/withdraw", controller::withdraw);
        app.post("/api/send", controller::sendMoney);
        app.post("/api/transfer", controller::transfer);
        app.post("/api/invest", controller::invest);
        app.post("/api/withdraw-investments", controller::withdrawInvestments);
        app.get("/api/health", controller::health);
        
        // Root endpoint
        app.get("/", ctx -> {
            ctx.result("Green Day Bank API Server is running. Use /api endpoints.");
        });
    }
    
    private static int getPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv != null && !portEnv.isEmpty()) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException e) {
                System.err.println("Invalid PORT environment variable, using default 7070");
            }
        }
        return 7070;
    }
}
