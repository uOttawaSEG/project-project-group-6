package project.group6.eams.utils;

public class Administrator extends User{

    public Administrator(String email,String password){
        super(email, password);
        this.userType = "Administrator";

    }

    /**
     * Allows administrator to reject a RegisterableUser (Organizer or Attendee) following their sign up attempt.
     *
     * @param user of type RegisterableUser
     */
    /**public void rejectRequest (RegisterableUser user ) {

    }**/
}