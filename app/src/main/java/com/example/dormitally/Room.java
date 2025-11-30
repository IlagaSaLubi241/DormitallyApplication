package com.example.dormitally;

public class Room {
    private int roomId;
    private String roomNum;
    private String roomType;
    private String roomStatus;
    private double roomPrice;
    private int roomCapacity;
    private String roomDescription;
    private final byte[] roomImage;

    public Room(int roomId, String roomNum, String roomType, String roomStatus, double roomPrice, int roomCapacity, String roomDescription, final byte[] roomImage) {
        this.roomId = roomId;
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.roomPrice = roomPrice;
        this.roomCapacity = roomCapacity;
        this.roomDescription = roomDescription;
        this.roomImage = roomImage;
    }

    public Room(String roomNum, String roomType, String roomStatus, double roomPrice, int roomCapacity, String roomDescription, final byte[] roomImage) {
        this.roomNum = roomNum;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        this.roomPrice = roomPrice;
        this.roomCapacity = roomCapacity;
        this.roomDescription = roomDescription;
        this.roomImage = roomImage;
    }

    public byte[] getRoomImage() {
        return roomImage;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
}
