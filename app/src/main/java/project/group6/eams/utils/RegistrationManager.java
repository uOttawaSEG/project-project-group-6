package project.group6.eams.utils;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;
import project.group6.eams.users.Administrator;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.users.RegisterableUser;
import project.group6.eams.users.User;

public class RegistrationManager {

    private final String usersReference;
    private final DatabaseManager users;

    public RegistrationManager (String usersReference) {
        this.usersReference = usersReference;
        users = new DatabaseManager(usersReference);
    }

    public void addUser (RegisterableUser user, RegistrationCallback callback) {
        if (user != null) {
            String userEmail = user.getEmail().toLowerCase().replaceAll(" ", "");
            users.readFromReference(userEmail, existingUserDoc -> {
                if (!existingUserDoc.exists()) {
                    users.writeToReference(userEmail, user);
                    callback.onSuccess();
                } else {
                    try {
                        User existingUser = userMapper(existingUserDoc);

                        if (existingUser.getUserType().equals("Administrator")) {
                            callback.onError(new ExistingUserException("User already in database"));
                        } else if (((RegisterableUser) existingUser).getRejectionStatus()) {
                            callback.onError(new RejectedUserException("User has been rejected by Admin"));
                        } else if (((RegisterableUser) existingUser).getApprovalStatus()) {
                            callback.onError(new ExistingUserException("User already in database"));
                        } else {
                            callback.onError(new PendingUserException("Request waiting for admin " +
                                    "approval"));
                        }
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            });
        }
    }

    public void checkForUser (String email, RegistrationCallback callback) {
        if (email != null) {
            users.readFromReference(email.toLowerCase().replaceAll(" ", ""), existingUserDoc -> {
                if (!existingUserDoc.exists()) {
                    callback.onError(new ExistingUserException("User not in database"));
                }
                try {
                    User existingUser = userMapper(existingUserDoc);
                    if (existingUser.getUserType().equals("Administrator")) {
                        callback.onSuccess(existingUser);
                    } else if (((RegisterableUser) existingUser).getRejectionStatus()) {
                        callback.onError(new RejectedUserException("User has been rejected by Admin"));
                    } else if (!((RegisterableUser) existingUser).getApprovalStatus()) {
                        callback.onError(new PendingUserException("Request waiting for admin " +
                                "approval"));
                    } else {
                        callback.onSuccess(existingUser);
                    }
                } catch (Exception e) {
                    if (e.getMessage() != null) {
                        Log.e("Database", e.getMessage());
                    } else {
                        Log.e("Database", "emptyMessage");
                    }
                    callback.onError(e);
                }
            });
        }
    }

    public void getAllRequestedUsers (RegistrationCallbackList callback) {
        ArrayList<User> requestedUsers = new ArrayList<>();
        users.readAllFromReference(usersList -> {
            try {
                Log.d("Database", usersList.toString());
                for (DocumentSnapshot doc : usersList) {
                    User user = userMapper(doc);
                    if (user == null) {} else if (!user.getUserType().equals("Administrator")) {
                        RegisterableUser rUser = (RegisterableUser) user;
                        if (!rUser.getApprovalStatus() && !rUser.getRejectionStatus()) {
                            requestedUsers.add(user);
                        }
                    }
                }
                callback.onSuccess(requestedUsers);
            } catch (Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
                callback.onError(e);
            }
        });
    }

    public void getAllRejectedUsers (RegistrationCallbackList callback) {
        ArrayList<User> rejectedUsers = new ArrayList<>();
        users.readAllFromReference(usersList -> {
            try {
                Log.d("Database", usersList.toString());
                for (DocumentSnapshot doc : usersList) {
                    User user = userMapper(doc);
                    if (user == null) {} else if (!user.getUserType().equals("Administrator")) {
                        RegisterableUser rUser = (RegisterableUser) user;
                        if (!rUser.getApprovalStatus() && rUser.getRejectionStatus()) {
                            rejectedUsers.add(user);
                        }
                    }
                }
                callback.onSuccess(rejectedUsers);
            } catch (Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
                callback.onError(e);
            }
        });
    }

    /**
     * Checks the database for the user's id (email), then updates the approval status or
     * rejection status of that user
     * to the boolean parameter value.
     *
     * @param email    is the User's ID
     * @param accepted boolean value that is true if their signup is approved, false if they have
     *                 been rejected
     * @param callback allows for exception handling.
     */
    public void changeUserStatus (String email, boolean accepted, RegistrationCallback callback) {
        if (email != null) {
            users.readFromReference(email.toLowerCase().replaceAll(" ", ""), userDoc -> {
                try {
                    RegisterableUser user = (RegisterableUser) userMapper(userDoc);
                    if (accepted) {
                        if (user.getRejectionStatus()) {
                            user.setRejectionStatus(false);
                        }
                        user.setApprovalStatus(true);
                    } else {
                        user.setRejectionStatus(true);
                    }

                    users.writeToReference(email.toLowerCase().replaceAll(" ", ""), user);
                    callback.onSuccess();
                } catch (Exception e) {
                    Log.e("Database", Objects.requireNonNull(e.getMessage()));
                    callback.onError(e);
                }
            });
        }
    }

    public static User userMapper (DocumentSnapshot document) {
        switch ((String) Objects.requireNonNull(document.get("userType"))) {
            case "Attendee":
                return document.toObject(Attendee.class);
            case "Administrator":
                return document.toObject(Administrator.class);
            case "Organizer":
                return document.toObject(Organizer.class);
            default:
                return null;
        }
    }

    public interface RegistrationCallback {
        void onSuccess ();

        void onSuccess (User type);

        void onError (Exception e);
    }

    public interface RegistrationCallbackList {
        void onSuccess (ArrayList<User> users);

        void onError (Exception e);
    }

}
