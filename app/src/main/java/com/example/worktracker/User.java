package com.example.worktracker;

public class User {
    private String userName;
    private String userAddress;
    private String userContactNumber;
    private String userTerritory;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String userAddress, String userContactNumber, String userTerritory){
        this.userName = userName;
        this.userAddress = userAddress;
        this.userContactNumber = userContactNumber;
        this.userTerritory = userTerritory;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public String getUserTerritory() {
        return userTerritory;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public void setUserTerritory(String userTerritory) {
        this.userTerritory = userTerritory;
    }
}
