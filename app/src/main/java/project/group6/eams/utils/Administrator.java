package project.group6.eams.utils;

public class Administrator extends User{

    public Administrator(String email,String password){
        super(email, password);
        userType = "Administrator";

    }

    public boolean getApprovalStatus() {return true;}
    public boolean getRejectionStatus() {return false;}
}