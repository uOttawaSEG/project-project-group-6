package project.group6.eams.users;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

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

    /**
     * Returns a boolean true if the event an attendee wants to register for conflicts with any of the events they have already
     * registered for and false if there are no conflicts
     * @param events the list of events that an attendee had already registered/requested
     * @param toRegister the event that the attendee wants to register for
     * @return a boolean specifying whether there are any conflicts
     */
    public boolean hasEventConflict(ArrayList<Event> events,Event toRegister){

        Date newEvStartTime = toRegister.getStartTime();
        Date newEvEndTime = toRegister.getEndTime();

        for(Event i: events){
            if(newEvStartTime.equals(i.getStartTime()))return true;

            if(newEvStartTime.before(i.getEndTime()) && (newEvEndTime.after(i.getEndTime()) || newEvStartTime.after(i.getStartTime()))){
                return true;

            }
        }
        return false;


    }

}
