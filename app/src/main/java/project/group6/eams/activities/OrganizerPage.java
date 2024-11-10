package project.group6.eams.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import project.group6.eams.R;
import project.group6.eams.activityUtils.AttendeeRequestAdapter;
import project.group6.eams.activityUtils.EventAdapter;

import project.group6.eams.utils.Attendee;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.utils.Organizer;
import project.group6.eams.utils.RegistrationManager;
import project.group6.eams.utils.User;

public class OrganizerPage extends AppCompatActivity {
    private String orgEmail;
    private String org;
    private Button logOffButton2;
    private Button createEvent_button;
    private Button pastEvents;
    private Button upcomingEvents;
    private TextView displayOrg;
    private RecyclerView recyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context = this;
        Intent intent = getIntent();
        orgEmail = intent.getStringExtra("email");

        if (org == null) {
            org = intent.getStringExtra("org_name");
        }

        displayOrg = findViewById(R.id.organizerEmail);
        displayOrg.setText(org);

        initViews();
        initListeners();
    }

    private void initViews() {
        logOffButton2 = findViewById(R.id.logOffButton2);
        createEvent_button = findViewById(R.id.createEvent_button);
        pastEvents = findViewById(R.id.past_events);
        upcomingEvents = findViewById(R.id.upcoming_events);
        recyclerView = findViewById(R.id.event_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListeners() {
        loadEvents("upcomingEvents");

        logOffButton2.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Logout successful. Redirecting to login " +
                    "page.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(OrganizerPage.this, MainActivity.class);
            startActivity(intent);
        });
        createEvent_button.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
            intent.putExtra("email", orgEmail);
            startActivity(intent);
        });

        pastEvents.setOnClickListener(v -> loadEvents("pastEvents"));

        upcomingEvents.setOnClickListener(v -> loadEvents("upcomingEvents"));

    }

    private void loadEvents(String eventType) {
        EventManager eventManager = new EventManager("Events");
        switch (eventType) {
            case "upcomingEvents":
                eventManager.getUpcomingEvents(org, new EventManager.EventCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Event> eventList) {
                        Log.d("Events", eventList.toString());
                        EventAdapter adapter = new EventAdapter(eventList, false, context);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Database", Objects.requireNonNull(e.getMessage()));
                    }

                });
                break;
            case "pastEvents":
                eventManager.getPastEvents(org, new EventManager.EventCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Event> eventList) {
                        Log.d("Events", eventList.toString());
                        EventAdapter adapter = new EventAdapter(eventList, true, context);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Database", Objects.requireNonNull(e.getMessage()));

                    }
                });
                break;
        }
    }

}