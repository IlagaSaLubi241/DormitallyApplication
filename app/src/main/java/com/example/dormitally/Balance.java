package com.example.dormitally;

public class Balance{
    int userId,balanceId;

    double wifiBalance,monthlyRentBalance,electricBalance,waterBalance,totalBalance,maintenanceFee;
    String balanceStatus;
    String dueDate,verification,date;

    public Balance(int userId, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double totalBalance, double maintenanceFee, String balanceStatus, String dueDate, String verification, String date) {
        this.userId = userId;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.totalBalance = totalBalance;
        this.maintenanceFee = maintenanceFee;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.date = date;
    }

    public Balance(int userId, int balanceId, double wifiBalance, double monthlyRentBalance, double electricBalance, double waterBalance, double totalBalance, double maintenanceFee, String balanceStatus, String dueDate, String verification, String date) {
        this.userId = userId;
        this.balanceId = balanceId;
        this.wifiBalance = wifiBalance;
        this.monthlyRentBalance = monthlyRentBalance;
        this.electricBalance = electricBalance;
        this.waterBalance = waterBalance;
        this.totalBalance = totalBalance;
        this.maintenanceFee = maintenanceFee;
        this.balanceStatus = balanceStatus;
        this.dueDate = dueDate;
        this.verification = verification;
        this.date = date;
    }

    public double getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(double maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    public double getWifiBalance() {
        return wifiBalance;
    }

    public void setWifiBalance(double wifiBalance) {
        this.wifiBalance = wifiBalance;
    }

    public double getMonthlyRentBalance() {
        return monthlyRentBalance;
    }

    public void setMonthlyRentBalance(double monthlyRentBalance) {
        this.monthlyRentBalance = monthlyRentBalance;
    }

    public double getElectricBalance() {
        return electricBalance;
    }

    public void setElectricBalance(double electricBalance) {
        this.electricBalance = electricBalance;
    }

    public double getWaterBalance() {
        return waterBalance;
    }

    public void setWaterBalance(double waterBalance) {
        this.waterBalance = waterBalance;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getBalanceStatus() {
        return balanceStatus;
    }

    public void setBalanceStatus(String balanceStatus) {
        this.balanceStatus = balanceStatus;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
