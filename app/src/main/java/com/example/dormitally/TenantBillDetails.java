package com.example.dormitally;

public class TenantBillDetails {
    private int userId,balanceId;
    private String fullName;
    private String roomNum;
    private double wifiBalance;
    private double monthlyRentBalance;
    private double electricBalance;
    private double waterBalance;
    private double maintenanceFee;
    private double totalBalance;
    String date;
    String paymentMethod;
    String balanceStatus;
    String dueDate,verification,paidDate;

    public TenantBillDetails(int userId,int balanceId, String fullName, String roomNum, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double maintenanceFee, double totalBalance, String paymentMethod, String balanceStatus, String dueDate, String verification, String paidDate,String date) {
        this.userId = userId;
        this.balanceId = balanceId;
        this.fullName = fullName;
        this.roomNum = roomNum;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.maintenanceFee = maintenanceFee;
        this.totalBalance = totalBalance;
        this.paymentMethod = paymentMethod;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.paidDate = paidDate;
        this.date = date;
    }

    public TenantBillDetails(String fullName, String roomNum, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double maintenanceFee, double totalBalance, String paymentMethod, String balanceStatus, String dueDate, String verification, String paidDate,String date) {
        this.fullName = fullName;
        this.roomNum = roomNum;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.maintenanceFee = maintenanceFee;
        this.totalBalance = totalBalance;
        this.paymentMethod = paymentMethod;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.paidDate = paidDate;
        this.date = date;
    }
    public TenantBillDetails(int userId, String fullName, String roomNum, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double maintenanceFee, double totalBalance, String paymentMethod, String balanceStatus, String dueDate, String verification, String paidDate,String date) {
        this.userId = userId;
        this.fullName = fullName;
        this.roomNum = roomNum;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.maintenanceFee = maintenanceFee;
        this.totalBalance = totalBalance;
        this.paymentMethod = paymentMethod;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.paidDate = paidDate;
        this.date = date;
    }
    public TenantBillDetails(int userId, int balanceId, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double maintenanceFee, double totalBalance, String balanceStatus, String dueDate, String verification, String date) {
        this.userId = userId;
        this.balanceId = balanceId;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.maintenanceFee = maintenanceFee;
        this.totalBalance = totalBalance;
        this.paymentMethod = paymentMethod;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }
    public int getBalanceId() {
        return balanceId;
    }

    public String getDate() {
        return date;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public double getWifiBalance() {
        return wifiBalance;
    }

    public double getMonthlyRentBalance() {
        return monthlyRentBalance;
    }

    public double getElectricBalance() {
        return electricBalance;
    }

    public double getWaterBalance() {
        return waterBalance;
    }

    public double getMaintenanceFee() {
        return maintenanceFee;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBalanceStatus() {
        return balanceStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getVerification() {
        return verification;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public void setWifiBalance(double wifiBalance) {
        this.wifiBalance = wifiBalance;
    }

    public void setMonthlyRentBalance(double monthlyRentBalance) {
        this.monthlyRentBalance = monthlyRentBalance;
    }

    public void setElectricBalance(double electricBalance) {
        this.electricBalance = electricBalance;
    }

    public void setWaterBalance(double waterBalance) {
        this.waterBalance = waterBalance;
    }

    public void setMaintenanceFee(double maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setBalanceStatus(String balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPaidDate() {
        return paidDate;
    }
}
