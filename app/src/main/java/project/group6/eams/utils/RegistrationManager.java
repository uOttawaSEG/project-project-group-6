package project.group6.eams.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Objects;

import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;

public class RegistrationManager {

    private final String usersReference;
    private final DatabaseManager<RegisterableUser> users;

    public RegistrationManager(String usersReference){
        this.usersReference = usersReference;
        users = new DatabaseManager<RegisterableUser>(usersReference);
    }

    public void addUser(RegisterableUser user, RegistrationCallback callback){
        String userEmail = user.getEmail().toLowerCase().replaceAll(" ","");
            users.readFromReference(userEmail, RegisterableUser.class, existingUser -> {
                try {
                    if (existingUser == null) {
                        users.writeToReference(userEmail, user);
                        callback.onSuccess();
                    } else if (existingUser.getRejectionStatus()) {
                        callback.onError(new RejectedUserException("User has been rejected by Admin"));
                    } else if (existingUser.getApprovalStatus()) {
                        callback.onError(new ExistingUserException("User already in database"));
                    } else {
                        callback.onError(new PendingUserException("Request waiting for admin approval"));

                    }
                } catch (Exception e) {
                    callback.onError(e);
                }
            });
        }
    public void checkForUser(String email, RegistrationCallback callback){
        users.readFromReference(email.toLowerCase().replaceAll(" ",""),RegisterableUser.class, existingUser -> {
            try{
                if (existingUser == null){
                    callback.onError(new ExistingUserException("User not in database"));
                } else if (existingUser.getRejectionStatus()){
                    callback.onError(new RejectedUserException("User has been rejected by Admin"));
                } else if (!existingUser.getApprovalStatus()){
                    callback.onError(new PendingUserException("Request waiting for admin approval"));
                }
                else{
                    callback.onSuccess(existingUser);
                }
            } catch (Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
                callback.onError(e);
            }
        });
    }

    public void getAllRequestedUsers(RegistrationCallbackList callback){
        ArrayList<User> requestedUsers = new ArrayList<>();
        users.readAllFromReference(RegisterableUser.class, usersList -> {
            try{
                Log.d("thingy1", usersList.toString());
                for (RegisterableUser user : usersList){
                    if (!user.getApprovalStatus() && !user.getRejectionStatus()){
                        requestedUsers.add(user);
                    }
                }
                callback.onSuccess(requestedUsers);
            } catch (Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
                callback.onError(e);
            }
        });
    }

    public void getAllRejectedUsers(RegistrationCallbackList callback){
        ArrayList<User> rejectedUsers = new ArrayList<>();
        users.readAllFromReference(RegisterableUser.class, usersList -> {
            for (RegisterableUser user : usersList){
                if (!user.getApprovalStatus() && user.getRejectionStatus()){
                    rejectedUsers.add(user);
                }
            }
            callback.onSuccess(rejectedUsers);
        });
    }


    public interface RegistrationCallback{
        void onSuccess();
        void onSuccess(User type);
        void onError(Exception e);
    }
    public interface RegistrationCallbackList{
        void onSuccess(ArrayList<User> users);
        void onError(Exception e);
    }
}
