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

import com.google.firebase.FirebaseApp;

import java.util.Map;
import java.util.Objects;

import project.group6.eams.R;
import project.group6.eams.utils.*;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;
    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private String inputEmailAddress;
    private String inputPassword;
    private DatabaseManager<String> databaseTypeCheck;

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

        FirebaseApp.initializeApp(this);
        databaseTypeCheck = new DatabaseManager<>("users");

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
                // needs to be changed to check database for email and respective password
                // different errors for different cases: a) email not in database and b) email doesn't match password
                String id = DatabaseManager.formatEmailAsId(inputEmailAddress);
                databaseTypeCheck.readFromReference(id, String.class, userType ->  {
                    if (userType == null){
                        Log.e("Database", "Email not in the database");

                    } else {
                        String subfolder = "users/"+userType;
                        switch (userType){
                            case "attendees":
                                new DatabaseManager<Attendee>(subfolder).readFromReference(id, Attendee.class, value -> {
                                    if (Objects.equals(value.getPassword(), inputPassword)){
                                        Intent intent = new Intent(MainActivity.this, AttendeePage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Log.e("Input", "Email doesn't match password");
                                    }
                                });
                                break;
                            case "organizers":
                                new DatabaseManager<Organizer>(subfolder).readFromReference(id, Organizer.class, value -> {
                                    if (Objects.equals(value.getPassword(), inputPassword)){
                                        Intent intent = new Intent(MainActivity.this, OrganizerPage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Log.e("Input", "Email doesn't match password");
                                    }
                                });
                                break;
                            case "administrators":
                                new DatabaseManager<Administrator>(subfolder).readFromReference(id, Administrator.class, value -> {
                                    if (Objects.equals(value.getPassword(), inputPassword)){
                                        Intent intent = new Intent(MainActivity.this, AdministratorPage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Log.e("Input", "Email doesn't match password");
                                    }
                                });
                                break;
                            default:
                                Log.e("Database","User type not recognized");
                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                                startActivity(intent);
                        }
                    }

                });
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