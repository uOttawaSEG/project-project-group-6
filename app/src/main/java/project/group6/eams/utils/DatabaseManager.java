package project.group6.eams.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager<E> {
    private final DatabaseReference databaseReference;
    public DatabaseManager(String reference){
        this.databaseReference = FirebaseDatabase.getInstance().getReference(reference);
    }

    public void sendToReference (String id, E data) throws IllegalArgumentException {
        if (id == null || data == null){
            throw new IllegalArgumentException("Data or ID given is null");
        };
        this.databaseReference.child(id).setValue(data)
            .addOnSuccessListener(s -> {
                Log.i("Database","Write to Database success");
            })
            .addOnFailureListener(f -> {
                Log.e("Database","Failed to write to database");
            });

    }

    public static String formatEmailAsId(String email){
        return email.replace(".",",");

    }
}

