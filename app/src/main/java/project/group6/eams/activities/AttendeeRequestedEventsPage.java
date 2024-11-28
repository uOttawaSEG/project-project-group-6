package project.group6.eams.activities;

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

import java.util.ArrayList;
import java.util.Objects;

import project.group6.eams.R;
import project.group6.eams.activityUtils.ActivityUtils;
import project.group6.eams.activityUtils.AttendeeEventAdapter;
import project.group6.eams.users.Attendee;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

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
}
