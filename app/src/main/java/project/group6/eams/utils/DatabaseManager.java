package project.group6.eams.utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.Map;

/**
 * Generic class for handling calls to the Firebase Database
 * @param <E>
 */
public class DatabaseManager<E> {

    /**
     * Interface with method onCallBack that will be called with a result
     * and can be overridden to handle the result
     * @param <E>
     */
    public interface DatabaseCallback<E>{
        void onCallback(E value);
    }

    //Stored reference to a path in the database
    private final DatabaseReference databaseReference;

    /**
     * Initializes the reference path
     * @param reference reference path
     */
    public DatabaseManager(String reference){
        this.databaseReference = FirebaseDatabase.getInstance().getReference(reference);
    }

    /**
     * Takes an id and generic data and creates a key value pair as id:data as a child
     * of the reference path
     * @param id key
     * @param data value assigned to key
     * @throws IllegalArgumentException when either id or data is null
     */
    public void writeToReference (String id, E data) throws IllegalArgumentException {
        if (id == null || data == null){
            throw new IllegalArgumentException("Data or ID given is null");
        }
        this.databaseReference.child(id).setValue(data)
            .addOnSuccessListener(s -> Log.i("Database","Write to Database success"))
            .addOnFailureListener(f -> Log.e("Database","Failed to write to database"));
    }

    /**
     * Asynchronously checks the databaseReference for the given id, once done checking, calls the
     * onCallback method from the interface DatabaseCallback which can be overridden to handle the value
     * from the database.
     * Resources used to help write this code:
     * - <a href="https://www.geeksforgeeks.org/asynchronous-synchronous-callbacks-java">...</a>
     * - <a href="https://firebase.google.com/docs/database/android/read-and-write">...</a>
     *
     * @param id key to look for in database
     * @param type class type expected from the reference
     * @param callback used to handle the value once retrieved
     */
    public void readFromReference(String id, Class<E> type, DatabaseCallback<E> callback){
        databaseReference.child(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot data = task.getResult();
                if (data.exists()){
                    Log.d("Database","Retrieved data: " + data.getValue());
                    E object = data.getValue(type);
                    if (object != null){
                        callback.onCallback(object);
                    }
                    else{
                        Log.e("Database", "Failed to convert data to object");
                        callback.onCallback(null);
                    }
                }
                else{
                    Log.e("Database","No such id exists");
                    callback.onCallback(null);
                }
            } else {
                Log.e("Database","Failed to retrieve from reference");
                callback.onCallback(null);
            }});
    }

    /**
     * Replaces all "." with "," in an email so that it can be stored as a key in
     * the database. Will also check if the email is valid before formatting
     *
     * @param email must conform to @InputUtils.isValidEmail()
     * @return email with all "." replaced with ","
     * @throws IllegalArgumentException if email does not conform to @InputUtils.isValidEmail()
     */
    public static String formatEmailAsId(String email) throws IllegalArgumentException{
        if (!InputUtils.isValidEmail(email)){
            throw new IllegalArgumentException("Cannot format as not valid email");
        }
        return email.replace(".",",");
    }
}