package project.group6.eams.utils;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Objects;

public class Organizer extends RegisterableUser{
    private String organizationName;

    private transient EventManager eventManager = new EventManager("Events");

    public Organizer () {
        super();
        this.userType = "Organizer";
    }

    /**
     * @param email
     * @param password
     * @param firstname
     * @param lastname
     * @param phoneNumber
     * @param address
     * @param organizationName
     */
    public Organizer (String email, String password, String firstname, String lastname,
                      String phoneNumber, String address, String organizationName) {
        super(email, password, firstname, lastname, phoneNumber, address);
        this.organizationName = organizationName;
        this.userType = "Organizer";
    }

    public String getOrganizationName () {return organizationName;}

    public void setOrganizationName (String organizationName) {
        this.organizationName =
                organizationName;
    }

    public void approveEventRequest(Event ev, String email){
        if (ev.attendees.get(email) != null){
            ev.attendees.put(email, "approved");
        }

        eventManager.updateEvent(ev, new EventManager.EventCallback() {
            @Override
            public void onSuccess() {
                Log.d("Database", "Successfully approved " +email+ "for event");
            }

            @Override
            public void onError(Exception e) {
                Log.e("Database", "Could not approve " +email);
            }
        });

    }

    public void approveAllEventRequests(Event ev){

        for(String requester : ev.attendees.keySet()){
            if (requester != null){
                ev.attendees.put(requester, "approved");
            }
        }

        eventManager.updateEvent(ev, new EventManager.EventCallback() {
            @Override
            public void onSuccess() {
              Log.d("Database", "Successfully updated event");
            }

            @Override
            public void onError(Exception e) {
                Log.e("Database","Could not update event");
            }
         });

    }

    public void rejectEventRequest(Event ev, String email){
        if (ev.attendees.get(email) != null){
            ev.attendees.put(email, "rejected");
        }

        eventManager.updateEvent(ev, new EventManager.EventCallback() {
            @Override
            public void onSuccess() {
                Log.d("Database", "Successfully rejected " +email+ "for event");
            }

            @Override
            public void onError(Exception e) {
                Log.e("Database", "Could not reject " +email);
            }
        });
    }

    public void deleteEvent(Event ev){ eventManager.removeEvent(ev.title, new EventManager.EventCallback() {
        @Override
        public void onSuccess() {
            Log.d("Database", "Successfully deleted " + ev.getTitle());
        }

        @Override
        public void onError(Exception e) {
            Log.e("Database","Could not delete" + ev.getTitle());
        }
    });

    }

}
