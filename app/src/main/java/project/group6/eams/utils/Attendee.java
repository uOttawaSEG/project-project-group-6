package project.group6.eams.utils;

public class Attendee extends User{

    public Attendee(){
        super();
        this.userType = "Attendee";
    }

    public Attendee(String firstname, String lastname, String email, String phoneNumber, String address, String password){
        super(firstname, lastname, email, phoneNumber, address, password);
        this.userType = "Attendee";
    }
}
