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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    protected void onCreate(Bundle savedInstanceState) {
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
    private void initViews(){
        loginButton = findViewById(R.id.loginButton);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        signUpButton = findViewById(R.id.signUpButton);
    }

    /**
     * Initializes & defines logic for listeners
     */
    private void initListeners(){
        editTextTextEmailAddress.addTextChangedListener(textWatcher);
        editTextTextPassword.addTextChangedListener(textWatcher);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,SignUpPage.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Determines what type of user the email address belongs to, then finds the user data associated with the email address
     * and compares input password to email's password, if the two match, switches to the appropriate welcome page.
     */
    private void handleLogin(){
        editTextTextEmailAddress.setError(null);
        editTextTextPassword.setError(null);

        if (inputEmailAddress == null){
            editTextTextEmailAddress.setError("Invalid Email.");
            Log.e("Input","Invalid Email");
            return;
        }
        RegistrationManager registrationManager = new RegistrationManager("Users");
        registrationManager.checkForUser(inputEmailAddress, new RegistrationManager.RegistrationCallback() {
            public void onSuccess(){}
            @Override
            public void onSuccess(User user) {
                if (Objects.equals(user.getPassword(), inputPassword)){
                    if (user.getUserType().equals("Attendee")){
                        Intent intent = new Intent(MainActivity.this, AttendeePage.class);
                        startActivity(intent); //Switching to the type's activity
                    } else if (user.getUserType().equals("Organizer")) {
                        Intent intent = new Intent(MainActivity.this, OrganizerPage.class);
                        startActivity(intent);
                    } else if (user.getUserType().equals("Administrator")) {
                        Intent intent = new Intent(MainActivity.this, AdministratorPage.class);
                        startActivity(intent);
                    } else {
                        editTextTextPassword.setError("Failed");
                        Log.e("Database", "Failed");
                    }
                }
                else{
                    editTextTextPassword.setError("Password doesn't match email.");
                    Log.e("Input", "Email doesn't match password");
                }
            }
            @Override
            public void onError(Exception e) {
                if (e instanceof RejectedUserException) {
                    editTextTextEmailAddress.setError("Email has been rejected by Admin");
                } else if (e instanceof PendingUserException) {
                    editTextTextEmailAddress.setError("Email is currently waiting to be processed by the Admin");
                } else if (e instanceof ExistingUserException) {
                    editTextTextEmailAddress.setError("Email does not exist");
                } else {
                    Toast.makeText(getApplicationContext(), "DATABASE ERROR.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Waiting for editText fields to change and updating variables
     */
    private final TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputEmailAddress = editTextTextEmailAddress.getText().toString();
            inputPassword = editTextTextPassword.getText().toString();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

}