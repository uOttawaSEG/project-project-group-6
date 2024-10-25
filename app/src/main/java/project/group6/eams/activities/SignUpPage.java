package project.group6.eams.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.Editable;
import android.text.TextUtils;
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
    private String address;
    private EditText street;
    private EditText city;
    private EditText province;
    private EditText postalCode;
    private EditText organization;
    private EditText password;
    private EditText password2;
    private CheckBox organizerCheckbox;

    private boolean checkboxIsChecked;

    private String inputFirstName;
    private String inputLastName;
    private String inputEmail;
    private String inputPhoneNumber;
    private String inputStreet;
    private String inputCity;
    private String inputProvince;
    private String inputPostalCode;
    private String inputOrganization;
    private String inputPassword;
    private String inputPasswordAgain;

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

        //Initializing firebase
        FirebaseApp.initializeApp(this);

        //Binding UI Elements & Assigning to listeners
        initViews();
        initListeners();

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
        street = findViewById(R.id.enterStreet);
        city = findViewById(R.id.enterCity);
        province = findViewById(R.id.enterProvince);
        postalCode = findViewById(R.id.enterPostalCode);
        organization = findViewById(R.id.enterOrganization);
        password = findViewById(R.id.enterPassword);
        password2 = findViewById(R.id.reenterPassword);
        backButton =  findViewById(R.id.backButton);
        submitButton = findViewById(R.id.submitButton);
    }

    /**
     * Initializes & defines logic for listeners
     */
    private void initListeners(){
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        phoneNumber.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        province.addTextChangedListener(textWatcher);
        postalCode.addTextChangedListener(textWatcher);
        organization.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        password2.addTextChangedListener(textWatcher);

        //Checkbox that toggles Organizer input
        organizerCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkboxIsChecked = ( (CheckBox)v).isChecked();
                if (checkboxIsChecked) {
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

        // Submit button pressed, validates all inputs and then adds user to the database
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] editTexts = {firstName,lastName,email,phoneNumber,street,city, province, postalCode, organization, password, password2};
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

                if (TextUtils.isEmpty(inputOrganization) && organization.getVisibility()==View.VISIBLE) {
                    allValidInputs = false;
                    organization.setError("Must include organization name.");
                }
                if (!InputUtils.isValidStreet(inputStreet)) {
                    allValidInputs = false;
                    street.setError("Must be input: Building Number, Street Name, and Street Suffix");
                }
                if (!InputUtils.isValidName(inputCity)) {
                    allValidInputs = false;
                    city.setError("Invalid City Name");
                }
                if (!InputUtils.isValidName(inputProvince)) {
                    allValidInputs = false;
                    province.setError("Invalid Province Name");
                }
                if (!InputUtils.isValidPostalCode(inputPostalCode)) {
                    allValidInputs = false;
                    postalCode.setError("Must be a Canadian postal code.");
                }
                address = InputUtils.addressCreator(inputStreet,inputCity,inputProvince,inputPostalCode);
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
                    String id = DatabaseManager.formatEmailAsId(inputEmail);
                    DatabaseManager <String> databaseMasterList = new DatabaseManager<>("MasterList");
                    databaseMasterList.readFromReference(id, String.class, value -> {
                        if (value == null){
                            Toast.makeText(getApplicationContext(), "Sign Up Successful! Please wait for admins to approve your request.", Toast.LENGTH_LONG).show();

                            // Submit data to database
                            String type;
                            if (checkboxIsChecked) {
                                type = "organizers";
                                toAdd = new Organizer(inputFirstName, inputLastName, inputEmail, inputPhoneNumber, address, inputPassword, inputOrganization);
                            }
                            else{
                                type = "attendees";
                                toAdd = new Attendee(inputFirstName, inputLastName, inputEmail, inputPhoneNumber, address, inputPassword);
                            }
                            new DatabaseManager<User>("Requests").writeToReference(type+"/"+id,toAdd);
                            new DatabaseManager<String>("RequestsList").writeToReference(id,type);
                            databaseMasterList.writeToReference(id,"requested");

                            Intent intent = new Intent(SignUpPage.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else if(value.equals("user")){
                            email.setError("Email already in use.");
                        }
                        else if(value.equals("requested")){
                            email.setError("Email is currently waiting to be processed by the Admin");
                        }
                        else if(value.equals("rejected")){
                            email.setError("Email has been rejected by Admin");
                        }
                    });

                }
            }

        });
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

    /**
     * Waiting for editText fields to change and updating variables
     */
    private final TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputFirstName = firstName.getText().toString();
            inputLastName = lastName.getText().toString();
            inputEmail = email.getText().toString();
            inputPhoneNumber = phoneNumber.getText().toString();
            inputStreet = street.getText().toString();
            inputCity = city.getText().toString();
            inputProvince = province.getText().toString();
            inputPostalCode = postalCode.getText().toString();
            inputOrganization =  organization.getText().toString();
            inputPassword = password.getText().toString();
            inputPasswordAgain = password2.getText().toString();
        }
        @Override
        public void afterTextChanged(Editable s) {}
    };

}