package com.example.dormitally;

import android.net.Uri;

public class TenantFullDetails {

    // ===== Accounts Table =====
    private int userId;
    private String fullName;
    private String userName;
    private String contactNum;
    private String email;
    private String status;
    private String accountStartDate;

    // ===== Person Info Table =====
    private String gender;
    private int age;
    private String address;
    private String civilStatus;
    private String reasonForRent;
    private Uri validId;

    // ===== Room Assignment Table =====
    private String roomNum;
    private double totalBalance;
    private String roomStartDate;

    public TenantFullDetails( String fullName, String userName, String contactNum, String email, String status, String accountStartDate, String gender, int age, String address, String civilStatus, String reasonForRent, Uri validId, String roomNum, double totalBalance, String roomStartDate) {
        this.fullName = fullName;
        this.userName = userName;
        this.contactNum = contactNum;
        this.email = email;
        this.status = status;
        this.accountStartDate = accountStartDate;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.civilStatus = civilStatus;
        this.reasonForRent = reasonForRent;
        this.validId = validId;
        this.roomNum = roomNum;
        this.totalBalance = totalBalance;
        this.roomStartDate = roomStartDate;
    }

    public TenantFullDetails() {}

    // ======= Getters & Setters =======

    // Accounts
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getContactNum() { return contactNum; }
    public void setContactNum(String contactNum) { this.contactNum = contactNum; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAccountStartDate() { return accountStartDate; }
    public void setAccountStartDate(String accountStartDate) { this.accountStartDate = accountStartDate; }


    // Person Info
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCivilStatus() { return civilStatus; }
    public void setCivilStatus(String civilStatus) { this.civilStatus = civilStatus; }

    public String getReasonForRent() { return reasonForRent; }
    public void setReasonForRent(String reasonForRent) { this.reasonForRent = reasonForRent; }

    public Uri getValidId() { return validId; }
    public void setValidId(Uri validId) { this.validId = validId; }


    // Room Assignment
    public String getRoomNum() { return roomNum; }
    public void setRoomNum(String roomNum) { this.roomNum = roomNum; }

    public double getTotalBalance() { return totalBalance; }
    public void setTotalBalance(double totalBalance) { this.totalBalance = totalBalance; }

    public String getRoomStartDate() { return roomStartDate; }
    public void setRoomStartDate(String roomStartDate) { this.roomStartDate = roomStartDate; }

}
