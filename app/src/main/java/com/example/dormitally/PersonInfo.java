package com.example.dormitally;

public class PersonInfo {
    int userId;
    String firstName;
    String lastName;
    String gender;
    int age;
    String civilStatus;
    String reasonForRent;
    byte[] validId;

    public PersonInfo(int userId, String firstName, String lastName, String gender, int age, String civilStatus, String reasonForRent, byte[] validId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.civilStatus = civilStatus;
        this.reasonForRent = reasonForRent;
        this.validId = validId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getReasonForRent() {
        return reasonForRent;
    }

    public void setReasonForRent(String reasonForRent) {
        this.reasonForRent = reasonForRent;
    }

    public byte[] getValidId() {
        return validId;
    }

    public void setValidId(byte[] validId) {
        this.validId = validId;
    }
}
