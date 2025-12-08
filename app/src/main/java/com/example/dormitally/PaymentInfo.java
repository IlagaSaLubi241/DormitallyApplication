package com.example.dormitally;

public class PaymentInfo{

    private int paymentId;
    private int balanceId;
    private int userId;
    private double amount;
    private String paymentMethod;
    private String referenceNum;
    private String paidDate;

    public PaymentInfo(int paymentId, int balanceId, int userId, double amount,
                       String paymentMethod, String referenceNum, String paidDate) {
        this.paymentId = paymentId;
        this.balanceId = balanceId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.referenceNum = referenceNum;
        this.paidDate = paidDate;
    }

    public PaymentInfo(int balanceId, int userId, double amount, String paymentMethod, String referenceNum, String paidDate) {
        this.balanceId = balanceId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.referenceNum = referenceNum;
        this.paidDate = paidDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNum() {
        return referenceNum;
    }

    public void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }
    // getters and setters...
}

