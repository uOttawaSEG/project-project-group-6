package project.group6.eams.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private final CollectionReference databaseReference;
    //public static final String INVALID_ID_CHARS = "[\\.\\$#\\[\\]/]";

    /**
     * Initializes the reference path
     * @param reference reference path
     */
    public DatabaseManager(String reference){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        this.databaseReference = database.collection(reference);
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
        this.databaseReference.document(id).set(data)
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
        databaseReference.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot data = task.getResult();
                if (data.exists()){
                    Log.d("Database","Retrieved data: " + data.getData());
                    E object = data.toObject(type);
                    if (object != null){
                        callback.onCallback(object);
                    }
                    else{
                        Log.e("Database", "Failed to convert data to object");
                        throw new RuntimeException("Failed to convert data to object");
                    }
                } else {
                    Log.e("Database","No such id exists");
                    callback.onCallback(null);
                }
            } else {
                Log.e("Database","Failed to retrieve from reference");
                throw new RuntimeException("Failed to retrieve from reference");
            }});
    }
}