package com.example.dormitally;

import android.net.Uri;

public class PersonInfo {
    int userId;
    String fullName;
    String gender;
    int age;
    String civilStatus;
    String reasonForRent;
    String address;
    Uri validId;

    public PersonInfo(int userId, String fullName, String gender, int age, String civilStatus, String reasonForRent, Uri validId,String address) {
        this.userId = userId;
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.civilStatus = civilStatus;
        this.reasonForRent = reasonForRent;
        this.validId = validId;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Uri getValidId() {
        return validId;
    }

    public void setValidId(Uri validId) {
        this.validId = validId;
    }
}
