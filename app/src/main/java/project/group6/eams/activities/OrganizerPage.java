package project.group6.eams.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import project.group6.eams.activityUtils.EventAdapter;

import project.group6.eams.users.Organizer;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

public class OrganizerPage extends AppCompatActivity {
    private Organizer organizer;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.organizer_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context = this;
        AppInfo appInfo = AppInfo.getInstance();
        organizer = (Organizer) appInfo.getCurrentUser();

        displayOrg = findViewById(R.id.organizerEmail_organizer_page);
        displayOrg.setText(organizer.getEmail());

        initViews();
        initListeners();
    }

    private void initViews() {
        logOffButton2 = findViewById(R.id.logOffButton_organizer_page);
        createEvent_button = findViewById(R.id.createEvent_button_organizer_page);
        pastEvents = findViewById(R.id.past_events_organizer_page);
        upcomingEvents = findViewById(R.id.upcoming_events_organizer_page);
        recyclerView = findViewById(R.id.event_recycler_view_organizer_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingEvents.setBackgroundResource(R.drawable.back_rounded_button_selected);
        pastEvents.setBackgroundResource(R.drawable.back_rounded_button);
    }

    private void initListeners() {
        loadEvents("upcomingEvents");

        logOffButton2.setOnClickListener(v -> {
            ActivityUtils.showInfoToast("Logout successful. Redirecting to login page.",context,+1200);
            Intent intent = new Intent(OrganizerPage.this, LoginPage.class);
            startActivity(intent);
        });
        createEvent_button.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
            startActivity(intent);
        });

        pastEvents.setOnClickListener(v -> {
            pastEvents.setBackgroundResource(R.drawable.back_rounded_button_selected);
            upcomingEvents.setBackgroundResource(R.drawable.back_rounded_button);
            loadEvents("pastEvents");
        });

        upcomingEvents.setOnClickListener(v -> {
            upcomingEvents.setBackgroundResource(R.drawable.back_rounded_button_selected);
            pastEvents.setBackgroundResource(R.drawable.back_rounded_button);
            loadEvents("upcomingEvents");
        });

    }

    private void loadEvents(String eventType) {
        EventManager eventManager = new EventManager("Events");
        switch (eventType) {
            case "upcomingEvents":
                eventManager.getUpcomingEvents(organizer.getOrganizationName(), new EventManager.EventCallbackList() {
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
                eventManager.getPastEvents(organizer.getOrganizationName(), new EventManager.EventCallbackList() {
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