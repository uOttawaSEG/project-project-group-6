package project.group6.eams.utils;

import java.io.Serializable;

public class Attendee extends RegisterableUser{

    public Attendee () {
        super();
        this.userType = "Attendee";
    }
    /**
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param phoneNumber
     * @param address
     */
    public Attendee (String email, String password, String firstname, String lastname,
                     String phoneNumber, String address) {
        super(email, password, firstname, lastname, phoneNumber, address);
        this.userType = "Attendee";
    }
}
