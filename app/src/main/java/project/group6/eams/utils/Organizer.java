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

    public void approveEventRequest(Event ev, String email){ eventManager.updateEvent(ev, new EventManager.EventCallback() {
        @Override
        public void onSuccess() {
            if (ev.attendees.get(email) != null){
                ev.attendees.put(email, "approved");
            }
        }

        @Override
        public void onError(Exception e) {

        }
    });

    }

    public void approveAllEventRequests(Event ev){ eventManager.updateEvent(ev, new EventManager.EventCallback() {
        @Override
        public void onSuccess() {
            for(Object requester : ev.attendees.keySet()){
                if (requester != null){
                    ev.attendees.put((String) requester, "approved");
                }
            }
        }

        @Override
        public void onError(Exception e) {

        }
    });

    }

    public void rejectEventRequest(Event ev, String email){eventManager.updateEvent(ev, new EventManager.EventCallback() {
        @Override
        public void onSuccess() {
            if (ev.attendees.get(email) != null){
                ev.attendees.put(email, "rejected");
            }
        }

        @Override
        public void onError(Exception e) {

        }
    });
    }

    public void deleteEvent(Event ev){ eventManager.removeEvent(ev.title, new EventManager.EventCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Exception e) {

        }
    });

    }

}
