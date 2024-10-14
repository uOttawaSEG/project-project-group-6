package project.group6.eams.activities;

import java.util.Arrays;
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

import com.google.firebase.FirebaseApp;

import project.group6.eams.R;
import project.group6.eams.utils.*;

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
    private CheckBox organizerCheckbox;

    private String inputFirstName;
    private String inputLastName;
    private String inputEmail;
    private String inputPhoneNumber;
    private String inputAddress;
    private String inputOrganization;
    private String inputPassword;
    private String inputPasswordAgain;

    private DatabaseManager<User> databaseManager;
    private User toAdd;


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

        //Binding UI Elements and initializing firebase
        initViews();
        FirebaseApp.initializeApp(this);
        databaseManager = new DatabaseManager<>("users");


        // assigning textListener for EditTexts
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        phoneNumber.addTextChangedListener(textWatcher);
        address.addTextChangedListener(textWatcher);
        organization.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        password2.addTextChangedListener(textWatcher);


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

        // Back button pressed, return to login page.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // returns to LogIn page
                Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // Submit button pressed
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] editTexts = {firstName,lastName,email,phoneNumber,address,organization, password, password2};
                clearErrors(editTexts);

                boolean allValidInputs = true;
                if (!InputUtils.isValidName(inputFirstName)) {
                    allValidInputs = false;
                    firstName.setError("First Name must be alphabetic characters only.");
                }
                if (!InputUtils.isValidName(inputLastName)) {
                    allValidInputs = false;
                    lastName.setError("Last Name must be alphabetic characters only.");
                }
                if (!InputUtils.isValidEmail(inputEmail)) {
                    allValidInputs = false;
                    email.setError("Invalid email format.");
                }
                if (!InputUtils.isValidPhoneNumber(inputPhoneNumber)) {
                    allValidInputs = false;
                    phoneNumber.setError("Invalid phone number.");
                }
                /*if (!InputUtils.isValidAddress(inputAddress)) {
                    allValidInputs = false;
                    address.setError("Invalid address.");
                }
                remove comment once method is implemented
                */
                String passwordInvalidReason = InputUtils.passwordChecker(inputPassword);
                if (!passwordInvalidReason.isEmpty()) { // means there is something wrong with the password
                    allValidInputs = false;
                    password.setError(passwordInvalidReason);
                } else if (!InputUtils.verifyPassword(inputPassword,inputPasswordAgain)){
                    allValidInputs = false;
                    password2.setError("Password must match previous input.");
                }

                // only runs once all inputs are valid
                if (allValidInputs) {
                    Toast.makeText(getApplicationContext(), "Sign Up Successful! Please wait for admins to approve your request.", Toast.LENGTH_LONG).show();

                    // Submit data to database
                    if (!(inputOrganization == null)){
                        toAdd = new Organizer(inputFirstName,inputLastName,inputEmail,inputPhoneNumber,inputAddress,inputPassword,inputOrganization);
                    }
                    else{
                        toAdd = new Attendee(inputFirstName, inputLastName, inputEmail, inputPhoneNumber, inputAddress, inputPassword);
                    }
                    String id = DatabaseManager.formatEmailAsId(inputEmail);
                    databaseManager.sendToReference(id,toAdd);

                    Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                    startActivity(intent);
                }
            }

        });

    }

    /**
     * Initializes views for the sign up page
     */
    private void initViews(){
        organizerCheckbox = findViewById(R.id.organizerCheckBox);
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
    }

    /**
     * Resets setError to null so that the previous value does not repeat for next input.
     *
     * @param editTexts array of all editTexts that are used for signup info and checked for validity.
     */
    private void clearErrors(EditText[] editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }
    // Waiting for editText fields to change and updating variables
    private final TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputFirstName = firstName.getText().toString();
            inputLastName = lastName.getText().toString();
            inputEmail = email.getText().toString();
            inputPhoneNumber = phoneNumber.getText().toString();
            inputAddress = address.getText().toString();
            inputOrganization =  organization.getText().toString();
            inputPassword = password.getText().toString();
            inputPasswordAgain = password2.getText().toString();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

}