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
import java.util.concurrent.atomic.AtomicInteger;

import project.group6.eams.execptions.ExistingEventException;

public class EventManager {

    private final String eventsReference;
    private final DatabaseManager events;
    private final RegistrationManager users; // used for methods that fetch lists of attendees

    public EventManager(String eventsReference) {
        this.eventsReference = eventsReference;
        events = new DatabaseManager(eventsReference);
        users = new RegistrationManager("Users");
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

                try {
                    events.writeToReference(eventTitle, event);
                    callback.onSuccess();
                } catch (Exception e) {
                    callback.onError(e);
                    Log.e("Database",Objects.requireNonNull(e.getMessage()));
                }

            } else { // already exists, might remove this so that addEvent can overwrite existing events?... but what if they make duplicate events TT

                Event eventExists= eventWrapper(existingEvent);
                callback.onError(new ExistingEventException( eventExists.getTitle() + " Event already created."));

            }
        });
    }

    /**
     * Updates the Event in the database.
     * Although the addEvent could be used to update an event, we must allow for the ability to tell an Organizer that
     * they already made an event.
     *
     * Calling this method means that they intend to update the event.
     *
     * @param event is the event to be updated
     * @param callback allows for error handling
     */
    public void updateEvent(Event event, EventCallback callback) {
        try {
          events.writeToReference(event.getTitle(),event);
          callback.onSuccess();
        } catch (Exception e) {
          callback.onError(e);
        }
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
        try {
            events.readFromReference(title, event -> {
                if (!event.exists()) {
                    callback.onError(new ExistingEventException("Event does not exist, cannot be removed."));
                } else { // event found
                    events.deleteFromReference(title);
                    callback.onSuccess();

                }
            });

        } catch (Exception e) {
            callback.onError(e);

        }
    }

    /**
     * Gets all events that have yet to occur from Database
     *
     * @param organizationName is the name of the organization whos upcoming events are to be found
     * @param callback allows for exception handling
     */
    public void getUpcomingEvents(String organizationName, EventCallbackList callback) {
        ArrayList<Event> upcomingEvents = new ArrayList<>();

        events.readAllFromReference(eventList -> {
            try {
                Log.d("Database", eventList.toString());
                for (DocumentSnapshot doc: eventList) {
                    Event event = eventWrapper(doc);
                    Date startDate = event.getStartTime();
                    if (!InputUtils.dateHasPassed(startDate)) { // date has not passed
                        upcomingEvents.add(event);
                    }
                }
                callback.onSuccess(upcomingEvents);

            } catch (Exception e) {
                callback.onError(e);
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }


        });
    }

    /**
     * Gets all events that have already occurred from Database
     *
     * @param organizationName is the name of the organization whos past events are to be found
     * @param callback allows for exception handling
     */
    public void getPastEvents(String organizationName, EventCallbackList callback) {
        ArrayList<Event> pastEvents = new ArrayList<>();

        events.readAllFromReference(eventList -> {
            try {
                Log.d("Database", eventList.toString());
                for (DocumentSnapshot doc: eventList) {
                    Event event = eventWrapper(doc);
                    Date endDate = event.getEndTime();
                    if (InputUtils.dateHasPassed(endDate)) { // date has passed
                        pastEvents.add(event);
                    }
                }
                callback.onSuccess(pastEvents);

            } catch (Exception e) {
                callback.onError(e);
                Log.e("Database", Objects.requireNonNull(e.getMessage()));
            }

        });
    }

    /**
     * Gets all attendee's IDs for a given event that have yet to have been approved or denied
     *
     * @param eventID is the Event whos requested attendees are to be found
     * @param callback allows for exception handling
     */
    public void getRequestedAttendees(String eventID, AttendeeCallbackList callback) {
        ArrayList<Attendee> requestedAttendees = new ArrayList<>();
        events.readFromReference(eventID, event -> {
            try {
                Log.d("Database","Finding attendee's from map within Event.");
                if (!event.exists()) {
                    callback.onError(new ExistingEventException("Event does not exist, cannot get requested attendees."));
                } else { // event found
                    Event e = eventWrapper(event);
                    Map<String, String> attendees = e.getAttendees();

                    int totalAttendees = attendees.size();
                    AtomicInteger processedAttendees = new AtomicInteger(0);

                    for (Map.Entry<String, String> attendee : attendees.entrySet()) {
                        String id = attendee.getKey();
                        String approvalStatus = attendee.getValue();
                        if (approvalStatus.equals("requested")) {
                            users.checkForUser(id, new RegistrationManager.RegistrationCallback() {
                                @Override
                                public void onSuccess() {}

                                @Override
                                public void onSuccess(User type) {
                                    requestedAttendees.add((Attendee) type);
                                }
                                @Override
                                public void onError(Exception e) {
                                    Log.e("Database", "Failed to get attendee");
                                }
                            });
                        }
                        processedAttendees.incrementAndGet();
                    }
                    if (processedAttendees.get() == totalAttendees) {
                        callback.onSuccess(requestedAttendees);
                    }
                }
            } catch (Exception e) {
                callback.onError(e);
            }
        });

    }

    /**
     * Gets all the attendee's IDs for a given event who's requests have been rejected.
     *
     * @param eventID is the Event in which the rejected attendees are to be found
     * @param callback allows for exception handling
     */
    public void getRejectedAttendees(String eventID, AttendeeCallbackList callback) {
        ArrayList<Attendee> rejectedAttendees = new ArrayList<>();
        events.readFromReference(eventID, event -> {
            try {
                Log.d("Database","Finding attendee's from map within Event.");
                if (!event.exists()) {
                    callback.onError(new ExistingEventException("Event does not exist, cannot get requested attendees."));
                } else { // event found
                    Event e = eventWrapper(event);
                    Map<String, String> attendees = e.getAttendees();

                    int totalAttendees = attendees.size();
                    AtomicInteger processedAttendees = new AtomicInteger(0);

                    for (Map.Entry<String, String> attendee : attendees.entrySet()) {
                        String id = attendee.getKey();
                        String approvalStatus = attendee.getValue();
                        if (approvalStatus.equals("rejected")) {
                            users.checkForUser(id, new RegistrationManager.RegistrationCallback() {
                                @Override
                                public void onSuccess() {}

                                @Override
                                public void onSuccess(User type) {
                                    rejectedAttendees.add((Attendee) type);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Database", "Failed to get attendee");
                                }
                            });
                        }

                        processedAttendees.incrementAndGet();
                    }
                    if (processedAttendees.get() == totalAttendees) {
                        callback.onSuccess(rejectedAttendees);
                    }
                }
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    /**
     * Gets all the attendee's for a given event who's requests have been approved.
     *
     * @param eventID is the Event in which the approved attendees are to be found
     * @param callback allows for exception handling
     */
    public void getApprovedAttendees(String eventID, AttendeeCallbackList callback) {
        ArrayList<Attendee> approvedAttendees = new ArrayList<>();
        events.readFromReference(eventID, event -> {
            try {
                Log.d("Database","Finding attendee's from map within Event.");
                if (!event.exists()) {
                    callback.onError(new ExistingEventException("Event does not exist, cannot get requested attendees."));
                } else { // event found
                    Event e = eventWrapper(event);
                    Map<String, String> attendees = e.getAttendees();

                    int totalAttendees = attendees.size();
                    AtomicInteger processedAttendees = new AtomicInteger(0);

                    for (Map.Entry<String, String> attendee : attendees.entrySet()) {
                        String id = attendee.getKey();
                        String approvalStatus = attendee.getValue();
                        if (approvalStatus.equals("approved")) {
                            users.checkForUser(id, new RegistrationManager.RegistrationCallback() {
                                @Override
                                public void onSuccess() {}

                                @Override
                                public void onSuccess(User type) {
                                    approvedAttendees.add((Attendee) type);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Database", "Failed to get attendee");
                                    callback.onError(e);
                                }
                            });
                        }
                        processedAttendees.incrementAndGet();
                    }

                    if (processedAttendees.get() == totalAttendees) {
                        callback.onSuccess(approvedAttendees);
                    }

                }
            } catch (Exception e) {
                callback.onError(e);
            }
        });
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

