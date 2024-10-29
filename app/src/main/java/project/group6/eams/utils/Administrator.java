package project.group6.eams.utils;
import android.util.Log;

import java.util.Objects;

import project.group6.eams.utils.RegistrationManager;

public class Administrator extends User{

    private RegistrationManager registrationManager = new RegistrationManager("Users");

    public Administrator(){
        super();
        this.userType = "Administrator";

    }
    public Administrator(String email,String password){
        super(email, password);
        this.userType = "Administrator";

    }

    /**
     * Allows administrator to reject a RegisterableUser (Organizer or Attendee) following their sign up attempt.
     *
     * @param user of type RegisterableUser
     */
    public void rejectRequest (RegisterableUser user) { // i can change this if u send email instead of RegisterableUser
        registrationManager.changeUserStatus(user.getEmail(),false, new RegistrationManager.RegistrationCallback() {

            @Override
            public void onSuccess() {
                Log.d("Users", user.toString() + "rejected");
            }

            @Override
            public void onSuccess(User type) {}
            
            @Override
            public void onError(Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }
        });
    }
}