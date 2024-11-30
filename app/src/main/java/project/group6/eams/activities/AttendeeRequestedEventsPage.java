package project.group6.eams.activities;

import static project.group6.eams.utils.InputUtils.dateHasPassed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import project.group6.eams.R;
import project.group6.eams.activityUtils.ActivityUtils;
import project.group6.eams.activityUtils.AttendeeEventAdapter;
import project.group6.eams.users.Attendee;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.utils.EventNotificationWorker;

public class AttendeeRequestedEventsPage extends AppCompatActivity {
    private TextView title;
    private TextView welcomeMessage;
    private Button backButton;
    private RecyclerView requestedEvents;
    private Attendee attendee;
    private Context context;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.attendee_requested_events_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.attendee_requested_events), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context =this;
        attendee = (Attendee)AppInfo.getInstance().getCurrentUser();
        initViews();
        initListeners();

    }
    private void initViews(){
        title = findViewById(R.id.requested_event_title_attendee);
        welcomeMessage = findViewById(R.id.welcomeText_requested_events_attendee);
        backButton = findViewById(R.id.back_button_requested_events_attendee);
        requestedEvents = findViewById(R.id.recycler_view_requested_events_attendee);
        requestedEvents.setLayoutManager(new LinearLayoutManager(this));
    }
    private void initListeners(){
        title.setText(attendee.getEmail()+"'s Requested Events");
        welcomeMessage.setText("Welcome "+attendee.getFirstname()+"! You are logged in as an Attendee");
        backButton.setOnClickListener(v -> {
            
            ActivityUtils.showInfoToast("Redirecting to event list.",context,+1200);
            Intent intent = new Intent(AttendeeRequestedEventsPage.this, AttendeePage.class);
            startActivity(intent);
        });

        EventManager eventManager = new EventManager("Events");
        eventManager.getRequestedEvents(attendee.getEmail(), new EventManager.EventCallbackList() {
            @Override
            public void onSuccess(ArrayList<Event> eventList) {
                Log.d("Events", eventList.toString());

                createWorkers(eventList); // for notifications

                AttendeeEventAdapter adapter = new AttendeeEventAdapter(eventList,context);
                requestedEvents.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

    /**
     * Creates workers for approved events
     */
    private void createWorkers(ArrayList<Event> eventList) {
        for (Event event : eventList) {
            if (!dateHasPassed(event.getStartTime()) && event.getAttendees().get(attendee.getEmail()).equals("approved")) {
                setEventNotificationWorker(event.getTitle(), attendee.getEmail(),event.getStartTime().getTime());
            }
        }
    }

    /**
     * method that sets worker to create reminders for each event
     *
     * @param eventName name of event
     * @param startTime long of startTime
     */
    private void setEventNotificationWorker(String eventName, String user, long startTime) {
        long currentTime = System.currentTimeMillis();
        long timeUntilNotification = startTime - currentTime - ( 24 * 60 * 60 * 1000 );

        if (timeUntilNotification < 0) { // less than 24 hours until event
            timeUntilNotification = 0;
        }
        // add data that will be passed to Worker to set reminder within 24 hours
        Data eventInfo = new Data.Builder().putString("eventName", eventName).putLong("startTime", startTime).build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(EventNotificationWorker.class)
                .setInputData(eventInfo)
                .addTag(eventName+user) // to check if worker has already been made
                .setInitialDelay(timeUntilNotification, TimeUnit.MILLISECONDS) // Set the delay
                .build(); // worker made that will send out notification within 24 hours

        WorkManager.getInstance(this)  // this is to check if a worker has alr been made since this is called upon whenever the page is opened.
                .getWorkInfosByTagLiveData(eventName+user) // event name is tag
                .observe(this, work -> {
                    if (work.isEmpty()) { // empty list, worker has not been made for the event
                        WorkManager.getInstance(this).enqueue(workRequest); // EventNotificationWorker should be made?
                        Log.i("EventNotificationWorker", "Worker made for event: " + eventName);
                    } else {
                        Log.i("EventNotificationWorker", "Worker already made for event: " + eventName);
                    }
                });

    }
}
