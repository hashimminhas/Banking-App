package com.greendaybank.dto;

import java.util.List;

/**
 * Response DTO for GET /api/users
 */
public class UsersResponse {
    private List<String> users;
    
    public UsersResponse(List<String> users) {
        this.users = users;
    }
    
    public List<String> getUsers() {
        return users;
    }
    
    public void setUsers(List<String> users) {
        this.users = users;
    }
}
