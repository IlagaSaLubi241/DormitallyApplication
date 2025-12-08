package com.example.dormitally;

public class IssueMessage {
    int messageId;
    int senderId;
    int recieverId;
    String messageText;
    String timestamp;
    String status;
    String title;

    public IssueMessage(int messageId, int senderId, int recieverId, String messageText, String timestamp, String status, String title) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.status = status;
        this.title = title;
    }

    public IssueMessage(int senderId, int recieverId, String messageText, String timestamp, String status, String title) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.status = status;
        this.title = title;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(int recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
