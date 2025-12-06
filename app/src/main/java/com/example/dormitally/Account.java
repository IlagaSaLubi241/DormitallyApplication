package com.example.dormitally;

public class Account {
    private int userId;
    private String username;
    private  String password;
    private  String role;
    private String contactNum;
    private String email;
    private String status;
    private String startDate;

    public Account(int userId, String username, String password, String role, String contactNum, String email, String status, String startDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.contactNum = contactNum;
        this.email = email;
        this.status = status;
        this.startDate = startDate;
    }
    public Account( String username, String password, String role, String contactNum, String email, String status, String startDate) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.contactNum = contactNum;
        this.email = email;
        this.status = status;
        this.startDate = startDate;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getContactNum() {
        return contactNum;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getStartDate() {
        return startDate;
    }
}
