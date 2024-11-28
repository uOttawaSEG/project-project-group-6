package project.group6.eams.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

public class AttendeePage extends AppCompatActivity {

    private Attendee attendee;
    private Button logOffButton;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private Button viewRequestedEventsButton;
    private Context context;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.attendee_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.context=this;
        AppInfo appInfo = AppInfo.getInstance();
        attendee = (Attendee) appInfo.getCurrentUser();
        initViews();
        initListeners();

    }

    private void initViews () {
        logOffButton = findViewById(R.id.logOffButton_attendee);
        recyclerView = findViewById(R.id.recycler_view_attendee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewRequestedEventsButton = findViewById(R.id.view_requested_events_button_attendeepage);
        searchView = findViewById(R.id.searchView_attendee);
    }

    private void initListeners () {
        logOffButton.setOnClickListener(v ->{
                ActivityUtils.showInfoToast("Logout successful. Redirecting to login page.",context,+1200);
                Intent intent = new Intent(AttendeePage.this, LoginPage.class);
                startActivity(intent);
        });
        viewRequestedEventsButton.setOnClickListener(v->{
                ActivityUtils.showInfoToast("Redirecting to requested events page.",context,+1200);
                Intent intent = new Intent(AttendeePage.this, AttendeeRequestedEventsPage.class);
                startActivity(intent);
        });

        EventManager eventManager = new EventManager("Events");
        eventManager.getUpcomingEvents("Attendee", new EventManager.EventCallbackList() {
            @Override
            public void onSuccess(ArrayList<Event> eventList) {
                Log.d("Events", eventList.toString());

                // Filter out events that the attendee has already registered for
                ArrayList<Event> unregisteredEvents = new ArrayList<>();
                for (Event event : eventList) {
                    if (!event.isRegistered(attendee.getEmail())) {
                        unregisteredEvents.add(event);
                    }
                }

                AttendeeEventAdapter adapter = new AttendeeEventAdapter(unregisteredEvents,context);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.filter(newText);
                        return false;
                    }
                });
            }
            @Override
            public void onError(Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }
        });




    }
}
