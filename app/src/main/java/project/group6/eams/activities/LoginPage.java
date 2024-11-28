package project.group6.eams.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import project.group6.eams.R;
import project.group6.eams.activityUtils.ActivityUtils;
import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;
import project.group6.eams.users.Administrator;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.users.User;
import project.group6.eams.utils.*;

public class LoginPage extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;
    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private String inputEmailAddress;
    private String inputPassword;
    private Context context;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context = this;
        //Binding UI Elements & Assigning to listeners
        initViews();
        initListeners();

    }

    /**
     * Initializes the views for the Login page
     */
    private void initViews () {
        loginButton = findViewById(R.id.loginButton_login_page);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress_login_page);
        editTextTextPassword = findViewById(R.id.editTextTextPassword_login_page);
        signUpButton = findViewById(R.id.signUpButton_login_page);
    }

    /**
     * Initializes & defines logic for listeners
     */
    private void initListeners () {
        editTextTextEmailAddress.addTextChangedListener(textWatcher);
        editTextTextPassword.addTextChangedListener(textWatcher);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                handleLogin();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Determines what type of user the email address belongs to, then finds the user data
     * associated with the email address
     * and compares input password to email's password, if the two match, switches to the
     * appropriate welcome page.
     */
    private void handleLogin () {
        editTextTextEmailAddress.setError(null);
        editTextTextPassword.setError(null);

        if (inputEmailAddress == null) {
            editTextTextEmailAddress.setError("Invalid Email.");
            Log.e("Input", "Invalid Email");
            return;
        }
        RegistrationManager registrationManager = new RegistrationManager("Users");
        registrationManager.checkForUser(inputEmailAddress,
                new RegistrationManager.RegistrationCallback() {
            public void onSuccess () {}
            @Override
            public void onSuccess (User user) {
                AppInfo appInfo = AppInfo.getInstance();
                if (Objects.equals(user.getPassword(), inputPassword)) {
                    switch (user.getUserType()) {
                        case "Attendee": {
                            appInfo.setCurrentUser((Attendee)user);
                            Intent intent = new Intent(LoginPage.this, AttendeePage.class);
                            startActivity(intent); //Switching to the type's activity
                            break;
                        }
                        case "Organizer": {
                            appInfo.setCurrentUser((Organizer)user);
                            Intent intent = new Intent(LoginPage.this, OrganizerPage.class);
                            startActivity(intent);
                            break;
                        }
                        case "Administrator": {
                            appInfo.setCurrentUser((Administrator)user);
                            Intent intent = new Intent(LoginPage.this, AdministratorPage.class);
                            startActivity(intent);
                            break;
                        }
                        default:
                            editTextTextPassword.setError("Failed");
                            Log.e("Database", "Failed");
                            break;
                    }
                } else {
                    editTextTextPassword.setError("Password doesn't match email.");
                    Log.e("Input", "Email doesn't match password");
                }
            }
            @Override
            public void onError (Exception e) {
                if (e instanceof RejectedUserException) {
                    ActivityUtils.showFailedToast("User has been rejected by the Admin, please contact (613) 111-1111 for more information",context,-600);
                } else if (e instanceof PendingUserException) {
                    ActivityUtils.showFailedToast("Your registration has not been approved yet by the administrator. " +
                            "Please try again another time.",context,-600);
                    editTextTextEmailAddress.setError("Email is currently waiting to be "+
                     "processed by the Admin");
                } else if (e instanceof ExistingUserException) {
                    ActivityUtils.showFailedToast("Email does not exist. Please sign up.",context,-600);
                } else {
                    if (e.getMessage() != null) {
                        ActivityUtils.showFailedToast(e.getMessage(),context,-600);
                    } else {
                        ActivityUtils.showFailedToast("Failed",context,-600);
                    }
                }
            }
        });
    }
    /**
     * Waiting for editText fields to change and updating variables
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            inputEmailAddress = editTextTextEmailAddress.getText().toString();
            inputPassword = editTextTextPassword.getText().toString();
        }
        @Override
        public void afterTextChanged (Editable s) {}
    };

}