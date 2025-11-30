package com.example.dormitally;

public class RoomAssignment {
    private int userId;
    private String roomNum;
    private String firstName;
    private String lastName;
    private String startDate;
    private String endDate;

    public RoomAssignment(int userId, String roomNum, String firstName, String lastName, String startDate, String endDate) {
        this.userId = userId;
        this.roomNum = roomNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public RoomAssignment(String roomNum, String firstName, String lastName, String startDate, String endDate) {
        this.roomNum = roomNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
