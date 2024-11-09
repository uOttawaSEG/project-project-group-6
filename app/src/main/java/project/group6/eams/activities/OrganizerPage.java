package project.group6.eams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import project.group6.eams.R;
import project.group6.eams.utils.Organizer;

public class OrganizerPage extends AppCompatActivity {
    private String organizerEmail;

    private Button logOffButton2;
    private Button createEvent_button;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        organizerEmail = intent.getStringExtra("EMAIL");

        initViews();
        initListeners();
    }

    private void initViews () {
        logOffButton2 = findViewById(R.id.logOffButton2);
        createEvent_button = findViewById(R.id.createEvent_button);

    }
    private void initListeners () {
        logOffButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Toast.makeText(getApplicationContext(), "Logout successful. Redirecting to login " +
                        "page.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(OrganizerPage.this, MainActivity.class);
                startActivity(intent);

            }
        });
        createEvent_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(OrganizerPage.this, CreateEventPage.class);
                startActivity(intent);
            }
        });
    }
}