package project.group6.eams.utils;
import android.util.Log;
import android.widget.Toast;

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
     * Allows administrator to reject a RegisterableUser (Organizer or Attendee) following their sign up attempt
     * by retrieving the user object using the email as the ID.
     *
     * @param emailID of type String, user's ID
     */
    public void rejectRequest (String emailID) { // i can change this if u send email instead of RegisterableUser
        registrationManager.changeUserStatus(emailID,false, new RegistrationManager.RegistrationCallback() {

            @Override
            public void onSuccess() {
                Log.d("Users", emailID + " rejected");
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