package project.group6.eams.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import project.group6.eams.R;
import project.group6.eams.activityUtils.ActivityUtils;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.utils.InputUtils;
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.RegistrationManager;
import project.group6.eams.users.User;

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
    private Organizer organizer;

    private Date startDate;
    private Date endDate;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event_page);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_event_page_organizer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        orgEmail = intent.getStringExtra("email");

        RegistrationManager users = new RegistrationManager("Users");

        users.checkForUser(orgEmail, new RegistrationManager.RegistrationCallback() {
            public void onSuccess() {}
            public void onSuccess(User user) {
                organizer = (Organizer) user;
            }
            public void onError(Exception e) {
                Log.e("Database", "Failed to get user");
            }
        });
        this.context = this;
        initViews();
        initListeners();

    }

    private void initViews() {
        eventTitle = findViewById(R.id.eventTitleEditText_create_event_organizer);
        eventDescription = findViewById(R.id.eventDescriptionEditText_create_event_organizer);
        street = findViewById(R.id.enterStreetNameEditText_create_event_organizer);
        city = findViewById(R.id.enterEventCityEditText_create_event_organizer);
        province = findViewById(R.id.enterEventProvinceEditText_create_event_organizer);
        postalCode = findViewById(R.id.enterEventPostalEditText_create_event_organizer);
        start_date = findViewById(R.id.start_dateEditText_create_event_organizer);
        end_date = findViewById(R.id.end_dateEditText_create_event_organizer);
        autoApproveCheckbox = findViewById(R.id.autoApproveCheckbox_create_event_organizer);
        start_date.setInputType(InputType.TYPE_NULL);
        end_date.setInputType(InputType.TYPE_NULL);
        backButton = findViewById(R.id.back_button_create_event_organizer);
        createEventButton = findViewById(R.id.createEventButton_create_event_organizer);
    }

    private void initListeners() {
        eventTitle.addTextChangedListener(textWatcher);
        eventDescription.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        province.addTextChangedListener(textWatcher);
        postalCode.addTextChangedListener(textWatcher);
        start_date.addTextChangedListener(textWatcher);

        //Shows date/time selector dialog and sets the start time if pressed.
        start_date.setOnClickListener(v-> showDateTimeSelector(start_date));

        //Shows date/time selector dialog and sets the end time if pressed.
        end_date.setOnClickListener(v-> showDateTimeSelector(end_date));

        //check box to make automatic approval true
        autoApproveCheckbox.setOnClickListener(v-> autoApproval = ((CheckBox) v).isChecked());

        //Returns to OrganizerPage if pressed.
        backButton.setOnClickListener(v-> {// returns to Organizer page
            Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
            intent.putExtra("email", orgEmail);
            startActivity(intent);
        });

        createEventButton.setOnClickListener(v-> {
            EditText[] editTexts = {eventTitle, street, city,
                    province, postalCode, start_date, end_date};
            clearErrors(editTexts);
            address = InputUtils.addressCreator(inputStreet, inputCity, inputProvince,
                    inputPostalCode);
            boolean allValidInputs = true;
            if (inputEventTitle != null && inputEventTitle.isEmpty()) {
                allValidInputs = false;
                eventTitle.setError("Please enter an event name.");
            }
            if (inputEventDescription != null && inputEventDescription.isEmpty()) {
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
            if (endDate == null) {
                allValidInputs = false;
                end_date.setError("Please select end date/time");
            }
            if (startDate == null) {
                allValidInputs = false;
                start_date.setError("Please select start date/time");
            } else if (endDate != null && startDate != null) {

                if (!InputUtils.isValidEventRuntime(startDate, endDate)) {

                    allValidInputs = false;
                    end_date.setError("Error. End of event cannot be before start of event.");
                }

            }

            if (allValidInputs) {
                Map<String, String> attendees = new HashMap<>();
                Event newEvent = new Event(inputEventTitle, inputEventDescription, address, startDate, endDate, autoApproval, organizer, attendees);

                EventManager eventManager = new EventManager("Events");
                eventManager.addEvent(newEvent, new EventManager.EventCallback() {

                    @Override
                    public void onSuccess() {
                        ActivityUtils.showInfoToast("Event successfully created.",context,+1200);
                        Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
                        intent.putExtra("email", orgEmail);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Exception e) {
                        ActivityUtils.showInfoToast("Event of this Title already exists",context,+1200);
                        eventTitle.setError("Title already in exists.");
                    }

                });
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

    private void showDateTimeSelector(EditText text) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override

            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                        text.setText(simpleDateFormat.format(c.getTime()));

                        if (text == start_date) {
                            startDate = c.getTime();
                        }
                        if (text == end_date) {
                            endDate = c.getTime();
                        }

                    }
                };

                new TimePickerDialog(CreateEventPage.this, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventPage.this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void clearErrors(EditText[] editTexts) {
        for (EditText editText : editTexts) {
            editText.setError(null);
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            inputEventTitle = eventTitle.getText().toString();
            inputEventDescription = eventDescription.getText().toString();
            inputStreet = street.getText().toString();
            inputCity = city.getText().toString();
            inputProvince = province.getText().toString();
            inputPostalCode = postalCode.getText().toString();

        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}