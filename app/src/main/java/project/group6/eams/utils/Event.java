package project.group6.eams.utils;

import java.util.Date;
import java.util.Map;

public class Event {

    public String title;
    public String description;
    public String eventAddress;
    public Date startTime;
    public Date endTime;
    public boolean automaticApproval;
    public User creator;
    public Map attendees; // (String id, String approval status (approved, requested, denied)

    public Event(String title, String description, String eventAddress, Date startTime, Date endTime, boolean automaticApproval, User creator, Map attendees){
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
    public User getCreator() {return creator;}
    public String getEventAddress(){return eventAddress;}
    public Date getEndTime() {return endTime;}
    public Date getStartTime() {return startTime;}
    public String getDescription() {return description;}
    public String getTitle() {return title;}

    //Setters
    public void setAutomaticApproval() {this.automaticApproval = automaticApproval;}
    public void setCreator() {this.creator = creator;}
    public void setEventAddress(){this.eventAddress = eventAddress;}
    public void setEndTime() {this.endTime = endTime;}
    public void setStartTime() {this.startTime = startTime;}
    public void setDescription() {this.description =description;}
    public void setTitle() {this.title = title;}
}
