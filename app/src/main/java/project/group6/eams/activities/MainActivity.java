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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import project.group6.eams.R;
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
                // needs to be changed to check database for email and respective password
                // different errors for different cases: a) email not in database and b) email doesn't match password
                if (InputUtils.isValidEmail(inputEmailAddress)){
                    Intent intent = new Intent(MainActivity.this,AttendeePage.class);
                    startActivity(intent);
                } else {
                    //Toast.makeText(getApplicationContext(), "Invalid Login", Toast.LENGTH_LONG).show();
                    editTextTextEmailAddress.setError("Invalid email");
                }

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