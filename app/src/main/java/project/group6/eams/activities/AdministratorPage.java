package project.group6.eams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import project.group6.eams.utils.RegistrationManager;
import project.group6.eams.users.User;
import project.group6.eams.activityUtils.UserAdapter;

public class AdministratorPage extends AppCompatActivity {
    private Button logOffButton3;
    private Button rejected_button;
    private Button requested_button;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrator_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        initListeners();
    }

    private void initViews () {
        logOffButton3 = findViewById(R.id.logOffButton3);
        rejected_button = findViewById(R.id.rejected_button);
        requested_button = findViewById(R.id.requested_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initListeners () {
        loadUsers("requested");
        logOffButton3.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), "Logout successful. Redirecting to login " +
                    "page.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AdministratorPage.this, MainActivity.class);
            startActivity(intent);
        });
        requested_button.setOnClickListener(v -> loadUsers("requested"));
        rejected_button.setOnClickListener(v -> loadUsers("rejected"));

    }
    private void loadUsers(String userStatuses){
        RegistrationManager registrationManager = new RegistrationManager("Users");
        switch (userStatuses){
            case "rejected":
                registrationManager.getAllRejectedUsers(new RegistrationManager.RegistrationCallbackList() {
                    @Override
                    public void onSuccess (ArrayList<User> usersList) {
                        Log.d("Users", usersList.toString());
                        UserAdapter adapter = new UserAdapter(usersList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError (Exception e) {
                        Log.e("Database", Objects.requireNonNull(e.getMessage()));
                    }

                });
                break;
            case "requested":
                registrationManager.getAllRequestedUsers(new RegistrationManager.RegistrationCallbackList() {
                    @Override
                    public void onSuccess (ArrayList<User> usersList) {
                        Log.d("Users", usersList.toString());
                        UserAdapter adapter = new UserAdapter(usersList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError (Exception e) {
                        Log.e("Database", Objects.requireNonNull(e.getMessage()));
                    }

                });
                break;
        }
    }
}