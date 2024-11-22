package project.group6.eams.activityUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;

public class AttendeeEventAdapter extends RecyclerView.Adapter<AttendeeEventAdapter.ViewHolder> {
    private final ArrayList<Event> events;
    private boolean isOnPastEventPage;

    private RecyclerView recyclerViewAttendee;
    Organizer organizer = new Organizer();

    public AttendeeEventAdapter (ArrayList<Event> events, boolean isOnPastEventPage) {
        this.events = events;
        this.isOnPastEventPage = isOnPastEventPage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView endTime;
        public TextView eventAddress;
        public TextView eventDescription;
        public Button requestToAttendButton;
        public Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_eventlistlayout);
            creator = itemView.findViewById(R.id.creator_eventlistlayout);
            startTime = itemView.findViewById(R.id.startTime_eventlistlayout);
            endTime = itemView.findViewById(R.id.endTime_eventlistlayout);
            eventAddress = itemView.findViewById(R.id.eventAddress_eventlistlayout);
            eventDescription = itemView.findViewById(R.id.description_eventlistlayout);
            requestToAttendButton = itemView.findViewById(R.id.attendee_list_button_eventlistlayout);
            delete = itemView.findViewById(R.id.delete_event_eventlistlayout);
        }
    }

    @NonNull
    @Override
    public AttendeeEventAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventlist_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull AttendeeEventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.creator.setText(event.getCreator().getEmail());
        holder.eventAddress.setText(event.getEventAddress());
        holder.eventDescription.setText(event.getDescription());
        holder.startTime.setText(event.getStartTime().toString());
        holder.endTime.setText(event.getEndTime().toString());
        holder.requestToAttendButton.setText("Request");
        holder.delete.setText("temp");
        /*request to attend holder.requestToAttendButton.setOnClickListener();*/
        /* prolly remove holder.delete.setOnClickListener(v -> organizer.deleteEvent(event));*/

    }
    @Override
    public int getItemCount () {
        return events.size();
    }

}
