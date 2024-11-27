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
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.InputUtils;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private final ArrayList<Event> events;
    private boolean isOnPastEventPage;
    private final Context context;
    private RecyclerView recyclerViewAttendee;
    private final Organizer organizer = (Organizer)AppInfo.getInstance().getCurrentUser();

    public EventAdapter (ArrayList<Event> events,boolean isOnPastEventPage, Context context) {
        this.events = events;
        this.context = context;
        this.isOnPastEventPage = isOnPastEventPage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView eventAddress;
        public TextView eventDescription;
        public Button attendee_list_button;
        public Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_eventlistlayout);
            creator = itemView.findViewById(R.id.creator_eventlistlayout);
            startTime = itemView.findViewById(R.id.startTime_eventlistlayout);
            eventAddress = itemView.findViewById(R.id.eventAddress_eventlistlayout);
            eventDescription = itemView.findViewById(R.id.description_eventlistlayout);
            attendee_list_button = itemView.findViewById(R.id.attendee_list_button_eventlistlayout);
            delete = itemView.findViewById(R.id.delete_event_eventlistlayout);
        }
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventlist_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.eventTitle.setText(event.getTitle());
        if (event.getCreator() != null){
            holder.creator.setText(event.getCreator().getEmail());
        } else {holder.creator.setText("Unknown Creator");}
        holder.eventAddress.setText(event.getEventAddress());
        holder.eventDescription.setText(event.getDescription());
        holder.startTime.setText(event.getStartTime().toString()+"-"+event.getEndTime().toString());
        holder.attendee_list_button.setOnClickListener(v -> {
            if (event.getAutomaticApproval()){
                organizer.approveAllEventRequests(event);
            }
            showAttendeeDialog(event);
        });

        if (!InputUtils.dateHasPassed(event.getStartTime())){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v -> {
                organizer.deleteEvent(event);
                events.remove(position);
                notifyDataSetChanged();
            });

        } else {
            holder.delete.setVisibility(View.GONE);
        }


    }
    @Override
    public int getItemCount () {
        return events.size();
    }

    public void showAttendeeDialog(Event event){
        loadAttendees(event,"requested");

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.view_attendees_dialog, null);
        dialogBuilder.setView(dialogView);

        Button acceptedButton = dialogView.findViewById(R.id.attendee_accepted_button_event_attendees_organizer);
        Button requestedButton = dialogView.findViewById(R.id.attendee_requested_button_event_attendees_organizer);
        Button rejectedButton = dialogView.findViewById(R.id.attendee_rejected_button_event_attendees_organizer);
        Button acceptAll = dialogView.findViewById(R.id.attendee_accept_all_button_event_attendees_organizer);
        recyclerViewAttendee = dialogView.findViewById(R.id.attendee_recycler_view_event_attendees_organizer);
        recyclerViewAttendee.setLayoutManager(new LinearLayoutManager(context));
        dialogBuilder.setTitle(event.getTitle()+ " Attendees");
        AlertDialog b = dialogBuilder.create();
        b.show();
        acceptedButton.setOnClickListener(v -> {
            loadAttendees(event,"accepted");
        });
        requestedButton.setOnClickListener(v -> {
            loadAttendees(event,"requested");
        });

        rejectedButton.setOnClickListener(v -> {
            loadAttendees(event,"rejected");
        });
        acceptAll.setOnClickListener(v -> {
            organizer.approveAllEventRequests(event);
            notifyDataSetChanged();
        });


    }

    private void loadAttendees(Event event, String attendeeType) {
        EventManager eventManager = new EventManager("Events");
        String eventID = event.getTitle();
        Log.d("Events", "Loading attendees for event: " + eventID + " with type: " + attendeeType);

        switch (attendeeType) {
            case "accepted":
                eventManager.getApprovedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Approved attendees: " + attendees.toString());
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No approved attendees found.");
                        }
                        AttendeeRequestAdapter adapter = new AttendeeRequestAdapter(attendees, event, organizer, context);
                        recyclerViewAttendee.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Database", "Error fetching approved attendees: " + e.getMessage());
                    }
                });
                break;
            case "rejected":
                eventManager.getRejectedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Rejected attendees: " + attendees.toString());
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No rejected attendees found.");
                        }
                        AttendeeRequestAdapter adapter = new AttendeeRequestAdapter(attendees, event, organizer, context);
                        recyclerViewAttendee.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Database", "Error fetching rejected attendees: " + e.getMessage());
                    }
                });
                break;
            case "requested":
                eventManager.getRequestedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Requested attendees: " + attendees.toString());
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No requested attendees found.");
                        }
                        AttendeeRequestAdapter adapter = new AttendeeRequestAdapter(attendees, event, organizer, context);
                        recyclerViewAttendee.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Database", "Error fetching requested attendees: " + e.getMessage());
                    }
                });
                break;
        }
    }


}
