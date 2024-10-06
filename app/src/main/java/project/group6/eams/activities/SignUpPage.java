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
import android.widget.Toast;
import android.widget.EditText;
import android.widget.CheckBox;

import project.group6.eams.R;

public class SignUpPage extends AppCompatActivity {
    private Button backButton;
    private Button submitButton;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private EditText organization;
    private EditText password;
    private EditText password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // figure out how to hide organization thing till checkbox is clicked

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox organizerCheckbox = findViewById(R.id.organizerCheckBox);

        firstName = findViewById(R.id.enterFirstName);
        lastName = findViewById(R.id.enterLastName);
        email = findViewById(R.id.enterEmail);
        phoneNumber = findViewById(R.id.enterPhoneNumber);
        address = findViewById(R.id.enterAddress);
        organization = findViewById(R.id.enterOrganization);
        password = findViewById(R.id.enterPassword);
        password2 = findViewById(R.id.reenterPassword);

        backButton =  findViewById(R.id.backButton);
        submitButton = findViewById(R.id.submitButton);

        organizerCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ( (CheckBox)v).isChecked();
                if (checked) {
                    organization.setVisibility(EditText.VISIBLE);
                } else {
                    organization.setVisibility(EditText.GONE);
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // returns to LogIn page
                Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // VALID INPUT CHECKING NEEDS TO BE IMPLEMENTED HERE

                // this is temporary
                boolean allValidInputs = true;
                if (allValidInputs) { // as of rn, it takes u back to login regardless of validity
                    Toast.makeText(getApplicationContext(), "Sign Up Successful! Please wait for admins to approve your request.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                    startActivity(intent);
                }
            }

        });

    }

    //Waiting for editText fields to change and updating variables
    private final TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputFirstName = firstName.getText().toString();
            String inputLastName = lastName.getText().toString();
            String inputEmail = email.getText().toString();
            String inputPhoneNumber = phoneNumber.getText().toString();
            String inputAddress = address.getText().toString();
            String inputOrganization =  organization.getText().toString();
            String inputPassword = password.getText().toString();
            String inputPasswordAgain = password2.getText().toString();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

}