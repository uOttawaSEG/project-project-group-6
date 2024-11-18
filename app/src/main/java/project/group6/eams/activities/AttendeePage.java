package project.group6.eams.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import project.group6.eams.activityUtils.AttendeeEventAdapter;
import project.group6.eams.activityUtils.EventAdapter;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

public class AttendeePage extends AppCompatActivity {

    private Button logOffButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        initListeners();

    }

    private void initViews () {
        logOffButton = findViewById(R.id.logOffButton);
        recyclerView = findViewById(R.id.recycler_view_attendee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void initListeners () {
        logOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Toast.makeText(getApplicationContext(), "Logout successful. Redirecting to login " +
                        "page.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AttendeePage.this, MainActivity.class);
                startActivity(intent);

            }
        });
        EventManager eventManager = new EventManager("Events");
        eventManager.getUpcomingEvents("Attendee", new EventManager.EventCallbackList() {
            @Override
            public void onSuccess(ArrayList<Event> eventList) {
                Log.d("Events", eventList.toString());
                AttendeeEventAdapter adapter = new AttendeeEventAdapter(eventList, false);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(Exception e) {
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }
        });
    }
}
