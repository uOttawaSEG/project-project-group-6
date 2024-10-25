package project.group6.eams.utils;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public static final String INVALID_ID_CHARS = "[\\.\\$#\\[\\]/]";

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
     * @throws IllegalArgumentException when either id or data is null or when the given ID is not a valid Database Key
     */
    public void writeToReference (String id, E data) throws IllegalArgumentException {
        if (id == null || data == null){
            throw new IllegalArgumentException("Data or ID given is null");
        }
        if (id.matches(INVALID_ID_CHARS)){
            throw new IllegalArgumentException("Invalid ID: " + id);
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
     * @param type class type expected from the value associated with key
     * @param callback used to handle the value once retrieved
     * @throws IllegalArgumentException when either id or data is null or when the given ID is not a valid Database Key
     */
    public void readFromReference(String id, Class<E> type, DatabaseCallback<E> callback) throws IllegalArgumentException{
        if (id == null || type == null || callback == null){
            throw new IllegalArgumentException("Data or ID given is null");
        }
        if (id.matches(INVALID_ID_CHARS)){
            throw new IllegalArgumentException("Invalid ID: " + id);
        }
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
     * Replaces all invalid characters with "," in an email so that it can be stored as a key in
     * the database. Removes whitespace and changes to lowercase.
     * Will also check if the email is valid before formatting
     *
     * @param email must conform to @InputUtils.isValidEmail()
     * @return converted email or null if Invalid email
     */
    public static String formatEmailAsId(String email){
        if (!InputUtils.isValidEmail(email)){
            return null;
        }
        return email.replaceAll(INVALID_ID_CHARS,",");
    }
}