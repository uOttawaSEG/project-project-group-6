package project.group6.eams.utils;

public class Organizer extends User{
    private String organizationName;

    public Organizer(){
        super();
        this.userType = "Organizer";
    }

    public Organizer(String firstname, String lastname, String email, String phoneNumber, String address, String password, String organizationName){
        super(firstname, lastname, email, phoneNumber, address, password);
        this.organizationName = organizationName;
        this.userType = "Organizer";
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
