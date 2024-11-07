package project.group6.eams.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import project.group6.eams.execptions.ExistingEventException;

public class EventManager {

    private final String eventsReference;
    private final DatabaseManager events;

    public EventManager(String eventsReference) {
        this.eventsReference = eventsReference;
        events = new DatabaseManager(eventsReference);
    }

    /**
     * Checks database for the Event
     * if it doesn't exist, adds it to the Events collection.
     *
     * @param event is the Event object with all the information about the event
     * @param callback allows for exception handling
     */
    public void addEvent(Event event, EventCallback callback) {

        String eventTitle = event.getTitle();

        events.readFromReference(eventTitle, existingEvent -> {
            if (!existingEvent.exists()) { // event not already in database

                events.writeToReference(eventTitle, event);
                callback.onSuccess();

            } else { // already exists

                Event existingUser = eventWrapper(existingEvent);
                callback.onError(new ExistingEventException("Event already created."));

            }
        });
    }

    /**
     * Checks for Event with title.
     * If found, removes it from the list.
     * Error handling(?)uhhhhh adding soon
     *
     * @param title is the title of the event that is to be removed
     * @param callback allows for exception handling
     */
    public void removeEvent(String title, EventCallback callback) {

    }

    /**
     * Gets all events that have yet to occur from Database
     *
     * @param callback allows for exception handling
     */
    public void getUpcomingEvents(EventCallbackList callback) {

    }

    /**
     * Gets all events that have already occurred from Database
     *
     * @param callback allows for exception handling
     */
    public void getPastEvents(EventCallbackList callback) {

    }

    /**
     * Gets all attendee's for a given event that have yet to have been approved or denied
     *
     * @param event is the Event whos requested attendees are to be found
     * @param callback allows for exception handling
     */
    public void getRequestedAttendees(Event event, AttendeeCallbackList callback) {

    }

    /**
     * Gets all the attendee's for a given event who's requests have been rejected.
     *
     * @param event is the Event in which the rejected attendees are to be found
     * @param callback allows for exception handling
     */
    public void getRejectedAttendees(Event event, AttendeeCallbackList callback) {

    }

    /**
     * Gets all the attendee's for a given event who's requests have been approved.
     *
     * @param event is the Event in which the approved attendees are to be found
     * @param callback allows for exception handling
     */
    public void getApprovedAttendees(Event event, AttendeeCallbackList callback) {

    }

    /**
     * Typecasting the data from the database into type Event so information
     * can be used/altered.
     *
     * @param documentSnapshot is the DocumentSnapshot read from the database
     * @return the documentSnapshot as type Event
     */
    public Event eventWrapper(DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(Event.class);
    }




    public interface EventCallback {
        void onSuccess ();

        void onError (Exception e);
    }

    public interface EventCallbackList {
        void onSuccess (ArrayList<Event> events);

        void onError (Exception e);
    }

    public interface AttendeeCallbackList {
        void onSuccess (ArrayList<Attendee> attendees);

        void onError (Exception e);
    }
}

