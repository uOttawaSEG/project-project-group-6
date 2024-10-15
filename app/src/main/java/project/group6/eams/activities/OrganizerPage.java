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
    private Button logOffButton2;
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
        initViews();
        initListeners();
    }

    private void initViews() {
        logOffButton2 = findViewById(R.id.logOffButton2);

    }
    private void initListeners(){
        logOffButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Logout successful. Redirecting to login page.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(OrganizerPage.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}