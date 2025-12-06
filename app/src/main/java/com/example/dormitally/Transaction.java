package com.example.dormitally;

public class Transaction {

    private int transactionId;
    private int userId;
    private int balanceId;
    private double amount;
    private String date;
    private String paymentMethod;
    private String referenceNum;

    public Transaction() {}

    public Transaction(int transactionId, int userId, int balanceId, double amount,
                       String date, String paymentMethod, String referenceNum) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.balanceId = balanceId;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.referenceNum = referenceNum;
    }

    // GETTERS AND SETTERS
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getBalanceId() { return balanceId; }
    public void setBalanceId(int balanceId) { this.balanceId = balanceId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getReferenceNum() { return referenceNum; }
    public void setReferenceNum(String referenceNum) { this.referenceNum = referenceNum; }
}
