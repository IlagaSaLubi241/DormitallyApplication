
package com.example.dormitally;

public class RoomAssignment {

    private int assignmentId,userId;
    private double totalBalance;
    private String roomNum;

    public RoomAssignment(int assignmentId, double totalBalance, int userId, String roomNum) {
        this.assignmentId = assignmentId;
        this.totalBalance = totalBalance;
        this.userId = userId;
        this.roomNum = roomNum;
    }

    public RoomAssignment( int userId,double totalBalance, String roomNum) {
        this.totalBalance = totalBalance;
        this.userId = userId;
        this.roomNum = roomNum;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public int getUserId() {
        return userId;
    }

    public String getRoomNum() {
        return roomNum;
    }

}
