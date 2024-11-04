package project.group6.eams.utils;

import com.google.firebase.Timestamp;

public class RegisterableUser extends User {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String address;
    private Timestamp requestTime;
    private boolean approvalStatus;
    private boolean rejectionStatus;

    public RegisterableUser () {
        super();
        this.approvalStatus = false;
        this.rejectionStatus = false;
    }

    /**
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param phoneNumber
     * @param address
     */
    public RegisterableUser (String email, String password, String firstname, String lastname,
                             String phoneNumber, String address) {
        super(email, password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.approvalStatus = false;
        this.rejectionStatus = false;
    }

    //Getters
    public String getFirstname () {return firstname;}

    public String getLastname () {return lastname;}

    public String getPhoneNumber () {return phoneNumber;}

    public String getAddress () {return address;}

    public boolean getApprovalStatus () {return approvalStatus;}

    public boolean getRejectionStatus () {return rejectionStatus;}

    public Timestamp getRequestTime () {return requestTime;}

    //Setters
    public void setFirstname (String firstname) {this.firstname = firstname;}

    public void setLastname (String lastname) {this.lastname = lastname;}

    public void setPhoneNumber (String phoneNumber) {this.phoneNumber = phoneNumber;}

    public void setAddress (String address) {this.address = address;}

    public void setApprovalStatus (boolean approvalStatus) {this.approvalStatus = approvalStatus;}

    public void setRejectionStatus (boolean rejectionStatus) {
        this.rejectionStatus =
                rejectionStatus;
    }

    public void setRequestTime (Timestamp requestTime) {this.requestTime = requestTime;}
}

