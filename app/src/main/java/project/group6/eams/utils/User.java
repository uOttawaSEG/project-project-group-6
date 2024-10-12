package project.group6.eams.utils;

public abstract class User {
    private boolean loginStatus;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;

    public User(String firstname, String lastname, String email, String phoneNumber, String address, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.loginStatus = false;
    }

    //Getters
    public boolean isLoggedIn() {return loginStatus;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}

    //Setters
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public void setEmail(String email) {this.email = email;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}

    public void login(){
        this.loginStatus = true;
    }
    public void logout(){
        this.loginStatus = false;
    }

}
