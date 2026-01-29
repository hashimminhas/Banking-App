package com.greendaybank.controller;

import com.greendaybank.dto.*;
import com.greendaybank.service.BankingService;
import io.javalin.http.Context;

/**
 * Controller handling all API endpoints
 */
public class BankingController {
    private final BankingService bankingService;
    
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }
    
    /**
     * GET /api/users
     */
    public void getUsers(Context ctx) {
        UsersResponse response = new UsersResponse(bankingService.getAllUserNames());
        ctx.json(response);
    }
    
    /**
     * POST /api/balance
     */
    public void getBalance(Context ctx) {
        try {
            BalanceRequest request = ctx.bodyAsClass(BalanceRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            BalanceResponse response = bankingService.getBalance(request.getUser());
            ctx.json(response);
            
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/deposit
     */
    public void deposit(Context ctx) {
        try {
            AmountRequest request = ctx.bodyAsClass(AmountRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Amount must be positive"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            bankingService.deposit(request.getUser(), request.getAmount());
            ctx.json(new SuccessResponse("success", "Deposit completed"));
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Insufficient cash")) {
                ctx.status(409).json(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
            } else {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", e.getMessage()));
            }
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/withdraw
     */
    public void withdraw(Context ctx) {
        try {
            AmountRequest request = ctx.bodyAsClass(AmountRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Amount must be positive"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            bankingService.withdraw(request.getUser(), request.getAmount());
            ctx.json(new SuccessResponse("success", "Withdrawal completed"));
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                ctx.status(409).json(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
            } else {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", e.getMessage()));
            }
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/send
     */
    public void sendMoney(Context ctx) {
        try {
            SendMoneyRequest request = ctx.bodyAsClass(SendMoneyRequest.class);
            
            if (request.getFrom() == null || request.getFrom().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "From user is required"));
                return;
            }
            
            if (request.getTo() == null || request.getTo().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "To user is required"));
                return;
            }
            
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Amount must be positive"));
                return;
            }
            
            if (!bankingService.userExists(request.getFrom())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "From user not found"));
                return;
            }
            
            if (!bankingService.userExists(request.getTo())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "To user not found"));
                return;
            }
            
            if (request.getFrom().equals(request.getTo())) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Cannot send money to yourself"));
                return;
            }
            
            bankingService.sendMoney(request.getFrom(), request.getTo(), request.getAmount());
            ctx.json(new SuccessResponse("success", "Money sent successfully"));
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                ctx.status(409).json(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
            } else {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", e.getMessage()));
            }
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/transfer
     */
    public void transfer(Context ctx) {
        try {
            TransferRequest request = ctx.bodyAsClass(TransferRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (request.getDirection() == null || request.getDirection().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Direction is required"));
                return;
            }
            
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Amount must be positive"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            bankingService.transfer(request.getUser(), request.getDirection(), request.getAmount());
            ctx.json(new SuccessResponse("success", "Transfer completed"));
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                ctx.status(409).json(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
            } else {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", e.getMessage()));
            }
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/invest
     */
    public void invest(Context ctx) {
        try {
            InvestRequest request = ctx.bodyAsClass(InvestRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (request.getFund() == null || request.getFund().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Fund is required"));
                return;
            }
            
            if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "Amount must be positive"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            bankingService.invest(request.getUser(), request.getFund(), request.getAmount());
            ctx.json(new SuccessResponse("success", "Investment completed"));
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Insufficient funds")) {
                ctx.status(409).json(new ErrorResponse("INSUFFICIENT_FUNDS", e.getMessage()));
            } else {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", e.getMessage()));
            }
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * POST /api/withdraw-investments
     */
    public void withdrawInvestments(Context ctx) {
        try {
            WithdrawInvestmentsRequest request = ctx.bodyAsClass(WithdrawInvestmentsRequest.class);
            
            if (request.getUser() == null || request.getUser().trim().isEmpty()) {
                ctx.status(400).json(new ErrorResponse("BAD_REQUEST", "User is required"));
                return;
            }
            
            if (!bankingService.userExists(request.getUser())) {
                ctx.status(404).json(new ErrorResponse("NOT_FOUND", "User not found"));
                return;
            }
            
            bankingService.withdrawAllInvestments(request.getUser());
            ctx.json(new SuccessResponse("success", "All investments withdrawn"));
            
        } catch (Exception e) {
            ctx.status(500).json(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }
    
    /**
     * GET /api/health
     */
    public void health(Context ctx) {
        ctx.json(new HealthResponse("ok"));
    }
}
