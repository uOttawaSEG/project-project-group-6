package project.group6.eams.utils;

public class RegisterableUser extends User{
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;

    public RegisterableUser(String email, String password, String firstname, String lastname, String phoneNumber, String address){
        super(email, password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    //Getters
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getAddress() {return address;}

    //Setters
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setAddress(String address) {this.address = address;}
}

