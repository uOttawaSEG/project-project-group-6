package project.group6.eams.users;

import android.util.Log;

import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

public class Attendee extends RegisterableUser {
    private static EventManager eventManager = new EventManager("Events");

    public Attendee () {
        super();
        this.userType = "Attendee";
    }
    /**
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param phoneNumber
     * @param address
     */
    public Attendee (String email, String password, String firstname, String lastname,
                     String phoneNumber, String address) {
        super(email, password, firstname, lastname, phoneNumber, address);
        this.userType = "Attendee";
    }

    /**
     * Allows an Attendee to request to register for an event
     *
     * @param event the Event that the Attendee want to request to attend
     */
    public void RequestToRegister(Event event){
        event.addAttendee(this.getEmail(),"requested");

        eventManager.updateEvent(event, new EventManager.EventCallback() {
            @Override
            public void onSuccess() {
                Log.d("Database", "Successfully added "+getEmail()+"'s request for event");
            }

            @Override
            public void onError(Exception e) {
                Log.d("Database", "Could not add event registration request");
            }
        });
    }

    /**
     * Allows an Attendee to cancel their registration for an event
     *
     * @param event the Event that the Attendee want to cancel their registration for
     */
    public void cancelRegistration(Event event){
        event.removeAttendee(this.getEmail());

        eventManager.updateEvent(event, new EventManager.EventCallback() {
            @Override
            public void onSuccess() {
                Log.d("Database", "Successfully canceled "+getEmail()+"'s request for event");
            }

            @Override
            public void onError(Exception e) {
                Log.d("Database", "Unable to cancel event registration request");
            }
        });
    }

}
