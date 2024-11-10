package project.group6.eams.activities;

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

import com.google.firebase.FirebaseApp;

import java.util.Objects;

import project.group6.eams.R;
import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;
import project.group6.eams.utils.*;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;
    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private String inputEmailAddress;
    private String inputPassword;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Binding UI Elements & Assigning to listeners
        initViews();
        initListeners();

    }

    /**
     * Initializes the views for the Login page
     */
    private void initViews () {
        loginButton = findViewById(R.id.loginButton);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        signUpButton = findViewById(R.id.signUpButton);
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
                Intent intent = new Intent(MainActivity.this, SignUpPage.class);
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
                if (Objects.equals(user.getPassword(), inputPassword)) {
                    switch (user.getUserType()) {
                        case "Attendee": {
                            Intent intent = new Intent(MainActivity.this, AttendeePage.class);
                            startActivity(intent); //Switching to the type's activity
                            break;
                        }
                        case "Organizer": {
                            Intent intent = new Intent(MainActivity.this, OrganizerPage.class);

                            intent.putExtra("email", inputEmailAddress);

                            startActivity(intent);

                            break;
                        }
                        case "Administrator": {
                            Intent intent = new Intent(MainActivity.this, AdministratorPage.class);
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
                    showToast(e);
                } else if (e instanceof PendingUserException) {
                    showToast(e);
                    //editTextTextEmailAddress.setError("Email is currently waiting to be
                    // processed by the Admin");
                } else if (e instanceof ExistingUserException) {
                    showToast(e);
                } else {
                    if (e.getMessage() != null) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }

    /**
     * Shows toast that appears for different User related Exceptions
     * <p>
     * Resource used to help learn how to make custom toasts:
     * <a href="https://additionalsheet.com/android-studio-custom-toast>...</a>
     *
     * @param e is the Exception type that determines the message shown.
     */
    private void showToast (Exception e) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.customizedToastLayout));

        TextView text = (TextView) layout.findViewById(R.id.customEmailToastText);

        if (e instanceof PendingUserException) {
            text.setText("Your registration has not been approved yet by the administrator. " +
                    "Please try again another time.");
        } else if (e instanceof ExistingUserException) {
            text.setText("Email does not exist. Please sign up.");
        }
        Toast emailToast = new Toast(getApplicationContext());
        emailToast.setGravity(Gravity.CENTER_VERTICAL, 0, -600);
        emailToast.setDuration(Toast.LENGTH_LONG);
        emailToast.setView(layout);
        emailToast.show();

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