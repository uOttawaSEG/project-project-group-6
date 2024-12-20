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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.CheckBox;

import com.google.firebase.Timestamp;

import project.group6.eams.R;
import project.group6.eams.activityUtils.ActivityUtils;
import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.users.RegisterableUser;
import project.group6.eams.users.User;
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

    private RegisterableUser toAdd;
    private Context context;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        // figure out how to hide organization thing till checkbox is clicked
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context = this;
        //Binding UI Elements & Assigning to listeners
        initViews();
        initListeners();

    }

    /**
     * Initializes views for the sign up page
     */
    private void initViews () {
        organizerCheckbox = findViewById(R.id.organizerCheckBox_signup_page);
        firstName = findViewById(R.id.enterFirstName_signup_page);
        lastName = findViewById(R.id.enterLastName_signup_page);
        email = findViewById(R.id.enterEmail_signup_page);
        phoneNumber = findViewById(R.id.enterPhoneNumber_signup_page);
        street = findViewById(R.id.enterStreet_signup_page);
        city = findViewById(R.id.enterCity_signup_page);
        province = findViewById(R.id.enterProvince_signup_page);
        postalCode = findViewById(R.id.enterPostalCode_signup_page);
        organization = findViewById(R.id.enterOrganization_signup_page);
        password = findViewById(R.id.enterPassword_signup_page);
        password2 = findViewById(R.id.reenterPassword_signup_page);
        backButton = findViewById(R.id.backButton_signup_page);
        submitButton = findViewById(R.id.submitButton_signup_page);
    }

    /**
     * Initializes & defines logic for listeners
     */
    private void initListeners () {
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
        organizerCheckbox.setOnClickListener(v ->{
            checkboxIsChecked = ((CheckBox) v).isChecked();
            if (checkboxIsChecked) {
                organization.setVisibility(EditText.VISIBLE);
            } else {
                organization.setVisibility(EditText.GONE);
            }
        });

        // Back button pressed, return to login page.
        backButton.setOnClickListener(v->{ // returns to LogIn page
            Intent intent = new Intent(SignUpPage.this, LoginPage.class);
            startActivity(intent);
        });

        // Submit button pressed, validates all inputs and then adds user to the database
        submitButton.setOnClickListener(v->{
            EditText[] editTexts = {firstName, lastName, email, phoneNumber, street, city,
                    province, postalCode, organization, password, password2};
            clearErrors(editTexts);

            boolean allValidInputs = true;
            if (!InputUtils.isValidName(inputFirstName)) {
                allValidInputs = false;
                firstName.setError("First Name must be alphabetic characters only.");
                Log.d("Input","First Name is invalid");
            }
            if (!InputUtils.isValidName(inputLastName)) {
                allValidInputs = false;
                lastName.setError("Last Name must be alphabetic characters only.");
            }
            if (!InputUtils.isValidEmail(inputEmail)) {
                allValidInputs = false;
                email.setError("Invalid email format. Correct Format: \n[Example@mail.com]");
            }
            if (!InputUtils.isValidPhoneNumber(inputPhoneNumber)) {
                allValidInputs = false;
                phoneNumber.setError("Invalid phone number. Format: \n[123-456-7890]");
            }

            if (TextUtils.isEmpty(inputOrganization) && organization.getVisibility() == View.VISIBLE) {
                allValidInputs = false;
                organization.setError("Must include organization name.");
            }
            if (!InputUtils.isValidStreet(inputStreet)) {
                allValidInputs = false;
                street.setError("Invalid Street Address. Format: \n[#### StreetName Suffix]");
            }
            if (!InputUtils.isValidName(inputCity)) {
                allValidInputs = false;
                city.setError("Invalid City Name.\nMust be alphabetic characters only.");
            }
            if (!InputUtils.isValidName(inputProvince)) {
                allValidInputs = false;
                province.setError("Invalid Province Name.\nMust be alphabetic characters only" +
                        ".");
            }
            if (!InputUtils.isValidPostalCode(inputPostalCode)) {
                allValidInputs = false;
                postalCode.setError("Must be a Canadian postal code of format:\n[A1A 1A1]");
            }
            address = InputUtils.addressCreator(inputStreet, inputCity, inputProvince,
                    inputPostalCode);
            String passwordInvalidReason = InputUtils.passwordChecker(inputPassword);
            if (!passwordInvalidReason.isEmpty()) { // means there is something wrong with
                // the password
                allValidInputs = false;
                password.setError(passwordInvalidReason);
            } else if (!InputUtils.verifyPassword(inputPassword, inputPasswordAgain)) {
                allValidInputs = false;
                password2.setError("Password must match previous input.");
            }

            // only runs once all inputs are valid
            if (allValidInputs) {
                if (checkboxIsChecked) {
                    toAdd = new Organizer(inputEmail, inputPassword, inputFirstName,
                            inputLastName, inputPhoneNumber, address, inputOrganization);
                } else {
                    toAdd = new Attendee(inputEmail, inputPassword, inputFirstName,
                            inputLastName, inputPhoneNumber, address);
                }
                toAdd.setRequestTime(Timestamp.now());
                RegistrationManager registrationManager = new RegistrationManager("Users");
                registrationManager.addUser(toAdd,
                        new RegistrationManager.RegistrationCallback() {
                    public void onSuccess (User user) {}
                    @Override
                    public void onSuccess () {
                        ActivityUtils.showInfoToast("Sign Up Successful! Please wait for admins to approve your request.", context,+1200);
                        Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError (Exception e) {
                        if (e instanceof RejectedUserException) {
                            email.setError("Email has been rejected by Admin");
                        } else if (e instanceof PendingUserException) {
                            email.setError("Email is currently waiting to be processed by the" +
                                    " Admin");
                        } else if (e instanceof ExistingUserException) {
                            email.setError("Email already in use.");
                        } else {
                            ActivityUtils.showFailedToast("DATABASE ERROR",context,-600);
                        }
                    }
                });
            }
        });

    }
    /**
     * Resets setError to null so that the previous value does not repeat for next input.
     *
     * @param editTexts array of all editTexts that are used for signup info and checked for
     *                  validity.
     */
    private void clearErrors (EditText[] editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    /**
     * Waiting for editText fields to change and updating variables
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {
            inputFirstName = firstName.getText().toString();
            inputLastName = lastName.getText().toString();
            inputEmail = email.getText().toString();
            inputPhoneNumber = phoneNumber.getText().toString();
            inputStreet = street.getText().toString();
            inputCity = city.getText().toString();
            inputProvince = province.getText().toString();
            inputPostalCode = postalCode.getText().toString();
            inputOrganization = organization.getText().toString();
            inputPassword = password.getText().toString();
            inputPasswordAgain = password2.getText().toString();
        }
        @Override
        public void afterTextChanged (Editable s) {}
    };

}