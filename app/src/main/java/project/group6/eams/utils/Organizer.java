package project.group6.eams.utils;

public class Organizer extends RegisterableUser {
    private String organizationName;

    public Organizer () {
        super();
        this.userType = "Organizer";
    }

    /**
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param phoneNumber
     * @param address
     * @param organizationName
     */
    public Organizer (String email, String password, String firstname, String lastname,
                      String phoneNumber, String address, String organizationName) {
        super(email, password, firstname, lastname, phoneNumber, address);
        this.organizationName = organizationName;
        this.userType = "Organizer";
    }

    public String getOrganizationName () {return organizationName;}

    public void setOrganizationName (String organizationName) {
        this.organizationName =
                organizationName;
    }
}
