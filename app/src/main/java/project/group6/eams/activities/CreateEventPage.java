package project.group6.eams.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

import project.group6.eams.R;
import project.group6.eams.utils.Attendee;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.utils.InputUtils;
import project.group6.eams.utils.Organizer;
import project.group6.eams.utils.RegistrationManager;
import project.group6.eams.utils.User;

public class CreateEventPage extends AppCompatActivity {


    private Button backButton;
    private Button createEventButton;
    private EditText eventTitle;
    private EditText eventDescription;
    private EditText street;
    private EditText city;
    private EditText province;
    private EditText postalCode;
    private CheckBox autoApproveCheckbox;
    private EditText start_date;
    private EditText end_date;

    private String inputEventTitle;
    private String inputEventDescription;
    private String inputStreet;
    private String inputCity;
    private String inputProvince;
    private String inputPostalCode;
    private String address;
    private boolean autoApproval;

    private String orgEmail;
    private User organizer;

    private Date startDate;
    private Date endDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event_page);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        orgEmail = intent.getStringExtra("email");


        RegistrationManager users = new RegistrationManager("Users");

        users.checkForUser(orgEmail, new RegistrationManager.RegistrationCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onSuccess(User user) {
                organizer = user;
            }

            @Override
            public void onError(Exception e) {
                Log.e("Database", "Failed to get user");
            }
        });
        initViews();
        initListeners();

    }

    private void initViews(){
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        street = findViewById(R.id.enterStreetName);
        city = findViewById(R.id.enterEventCity);
        province = findViewById(R.id.enterEventProvince);
        postalCode = findViewById(R.id.enterEventPostal);

        start_date=findViewById(R.id.start_date);
        end_date=findViewById(R.id.end_date);

        autoApproveCheckbox=findViewById(R.id.autoApprove);

        start_date.setInputType(InputType.TYPE_NULL);
        end_date.setInputType(InputType.TYPE_NULL);

        backButton = findViewById(R.id.back_button);
        createEventButton = findViewById(R.id.createEvent);

    }


    private void initListeners(){

        eventTitle.addTextChangedListener(textWatcher);
        eventDescription.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        province.addTextChangedListener(textWatcher);
        postalCode.addTextChangedListener(textWatcher);

        start_date.addTextChangedListener(textWatcher);

        //Shows date/time selector dialog and sets the start time if pressed.
        start_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDateTimeSelector(start_date);
            }
        });
        //Shows date/time selector dialog and sets the end time if pressed.
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeSelector(end_date);
            }
        });
        //check box to make automatic approval true
        autoApproveCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                autoApproval = ((CheckBox) v).isChecked();

            }
        });
        //Returns to OrganizerPage if pressed.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) { // returns to Organizer page
                Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
                intent.putExtra("email",orgEmail);
                startActivity(intent);
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                EditText[] editTexts = {eventTitle,street, city,
                        province, postalCode,start_date,end_date};
                clearErrors(editTexts);


                boolean allValidInputs = true;

                if (TextUtils.isEmpty(inputEventTitle)) {
                    allValidInputs = false;
                    eventTitle.setError("Please enter an event name.");
                }
                if(TextUtils.isEmpty(inputEventDescription)){
                    allValidInputs = false;
                    eventDescription.setError("Please describe your event.");
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
                if(startDate==null){
                    allValidInputs = false;
                    start_date.setError("Please select start date/time");
                }
                if(endDate==null){
                    allValidInputs = false;
                    end_date.setError("Please select end date/time");
                }
                if(startDate!=null && end_date!=null){
                    if(!InputUtils.isValidEventRuntime(startDate,endDate)){
                        allValidInputs = false;
                        end_date.setError("Error. End of event cannot be before start of event.");
                    }

                }
                address = InputUtils.addressCreator(inputStreet, inputCity, inputProvince,
                        inputPostalCode);
                if(allValidInputs) {
                    Map<String, String> attendees = new HashMap<>();
                    Event newEvent = new Event(inputEventTitle, inputEventDescription, address, startDate, endDate, autoApproval, organizer, attendees);

                    EventManager eventManager = new EventManager("Events");
                    eventManager.addEvent(newEvent, new EventManager.EventCallback() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "Event successfully created.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
                            intent.putExtra("email",orgEmail);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });

                }
            }
        });
    }

    /*
    *Shows dialog to select a date and then a time.
    *
    * The following reference was used to understand how to implement a DatePickerDialog:
    * https://www.geeksforgeeks.org/datepicker-in-android/
    *
    * @param text
    *
     */

    private void showDateTimeSelector(EditText text){
        Calendar c = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener(){
            @Override

            public  void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                        text.setText(simpleDateFormat.format(c.getTime()));

                        if(text ==start_date){
                            startDate = c.getTime();
                        }
                        else{
                            endDate =c.getTime();
                        }

                    }
                };

                new TimePickerDialog(CreateEventPage.this,timeSetListener,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventPage.this,dateSetListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void clearErrors (EditText[] editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {

            inputEventTitle = eventTitle.getText().toString();
            inputEventDescription = eventDescription.getText().toString();
            inputStreet = street.getText().toString();
            inputCity = city.getText().toString();
            inputProvince = province.getText().toString();
            inputPostalCode = postalCode.getText().toString();


        }
        @Override
        public void afterTextChanged (Editable s) {}
    };
}