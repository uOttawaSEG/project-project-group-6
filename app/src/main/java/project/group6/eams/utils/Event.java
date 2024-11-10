package project.group6.eams.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Event{

    public String title;
    public String description;
    public String eventAddress;
    public Date startTime;
    public Date endTime;
    public boolean automaticApproval;
    public Organizer creator;
    public Map<String, String> attendees; // (String id, String approval status (approved, requested, denied)

    public Event(){} // needed for firestore

    public Event(String title, String description, String eventAddress, Date startTime, Date endTime, boolean automaticApproval, Organizer creator, Map<String,String> attendees){
        this.automaticApproval = automaticApproval;
        this.creator = creator;
        this.eventAddress = eventAddress;
        this.endTime = endTime;
        this.description =description;
        this.startTime = startTime;
        this.title = title;
        this.attendees = attendees;

    }

    //Getters
    public boolean getAutomaticApproval() {return automaticApproval;}
    public Organizer getCreator() {return creator;}
    public String getEventAddress(){return eventAddress;}
    public Date getEndTime() {return endTime;}
    public Date getStartTime() {return startTime;}
    public String getDescription() {return description;}
    public String getTitle() {return title;}
    public Map getAttendees() {return attendees;}

    //Setters
    public void setAutomaticApproval(boolean automaticApproval) {this.automaticApproval = automaticApproval;}
    public void setCreator(Organizer creator) {this.creator = creator;}
    public void setEventAddress(String eventAddress){this.eventAddress = eventAddress;}
    public void setEndTime(Date endTime) {this.endTime = endTime;}
    public void setStartTime(Date startTime) {this.startTime = startTime;}
    public void setDescription(String description) {this.description =description;}
    public void setTitle(String title) {this.title = title;}

    // approval status = approved, rejected, requested ONLY
    public void addAttendee(String id, String approvalStatus) {
        attendees.put(id, approvalStatus);
    }
}
