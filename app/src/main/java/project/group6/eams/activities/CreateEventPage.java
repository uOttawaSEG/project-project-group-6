package project.group6.eams.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import java.util.SimpleTimeZone;

import project.group6.eams.R;

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
    private boolean autoApproval;


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
        createEventButton = findViewById(R.id.submitButton);

    }


    private void initListeners(){

        eventTitle.addTextChangedListener(textWatcher);
        eventDescription.addTextChangedListener(textWatcher);
        street.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        province.addTextChangedListener(textWatcher);
        postalCode.addTextChangedListener(textWatcher);


        start_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startDate=showDateTimeSelector(start_date);
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDate = showDateTimeSelector(end_date);
            }
        });

        autoApproveCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                autoApproval = ((CheckBox) v).isChecked();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) { // returns to Organizer page
                Intent intent = new Intent(CreateEventPage.this, OrganizerPage.class);
                startActivity(intent);
            }
        });

    }



    private Date showDateTimeSelector(EditText text){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener(){
            @Override

            public  void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                        text.setText(simpleDateFormat.format(calendar.getTime()));

                    }
                };

                new TimePickerDialog(CreateEventPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        return calendar.getTime();
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