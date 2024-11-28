package project.group6.eams.activityUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.RegisterableUser;
import project.group6.eams.users.User;
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
    private Button acceptAll;
    private Button acceptedButton;
    private Button rejectedButton;
    private Button requestedButton;


    public EventAdapter (ArrayList<Event> events,boolean isOnPastEventPage, Context context) {
        this.events = events;
        this.context = context;
        this.isOnPastEventPage = isOnPastEventPage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView endTime;
        public Button attendee_list_button;
        public Button delete;
        public LinearLayout eventlist_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_eventlistlayout);
            creator = itemView.findViewById(R.id.creator_eventlistlayout);
            startTime = itemView.findViewById(R.id.startTime_eventlistlayout);
            endTime = itemView.findViewById(R.id.endTime_eventlistlayout);
            attendee_list_button = itemView.findViewById(R.id.attendee_list_button_eventlistlayout);
            delete = itemView.findViewById(R.id.delete_event_eventlistlayout);
            eventlist_layout = itemView.findViewById(R.id.eventlist_layout);
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
        holder.startTime.setText(event.getStartTime().toString());
        holder.endTime.setText(event.getEndTime().toString());
        holder.attendee_list_button.setOnClickListener(v -> {
            if (event.getAutomaticApproval()){
                organizer.approveAllEventRequests(event);
            }
            showAttendeeDialog(event);
        });

        if (!InputUtils.dateHasPassed(event.getStartTime())){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(v -> {
                if(event.getAttendees().containsValue("approved")) {
                    ActivityUtils.showInfoToast("Cannot delete, event contains approved attendee(s)",context,+1200);

                }else{
                    organizer.deleteEvent(event);
                    events.remove(position);
                    notifyDataSetChanged();
                }
            });

        } else {
            holder.delete.setVisibility(View.GONE);
        }
        holder.eventlist_layout.setOnLongClickListener(v->{
            showEventInfo(event);
            return true;
        });


    }
    @Override
    public int getItemCount () {
        return events.size();
    }

    public void showAttendeeDialog(Event event){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context,R.style.dialogtheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.view_attendees_dialog, null);
        dialogBuilder.setView(dialogView);

        acceptedButton = dialogView.findViewById(R.id.attendee_accepted_button_event_attendees_organizer);
        requestedButton = dialogView.findViewById(R.id.attendee_requested_button_event_attendees_organizer);
        rejectedButton = dialogView.findViewById(R.id.attendee_rejected_button_event_attendees_organizer);
        acceptAll = dialogView.findViewById(R.id.attendee_accept_all_button_event_attendees_organizer);
        recyclerViewAttendee = dialogView.findViewById(R.id.attendee_recycler_view_event_attendees_organizer);
        recyclerViewAttendee.setLayoutManager(new LinearLayoutManager(context));
        dialogBuilder.setTitle(event.getTitle()+ " Attendees");
        AlertDialog b = dialogBuilder.create();
        loadAttendees(event,"requested");
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
        acceptedButton.setBackgroundTintList(null);
        requestedButton.setBackgroundTintList(null);
        rejectedButton.setBackgroundTintList(null);
        switch (attendeeType) {
            case "accepted":
                acceptedButton.setBackgroundColor(Color.parseColor("#7bc9c1")); //selected
                requestedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                rejectedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                eventManager.getApprovedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Approved attendees: " + attendees.toString());
                        acceptAll.setVisibility(View.VISIBLE);
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No approved attendees found.");
                            acceptAll.setVisibility(View.VISIBLE);
                            acceptAll.setText("None Approved");
                            acceptAll.setBackgroundColor(Color.parseColor("#ee645f"));
                            acceptAll.setBackgroundTintList(null);
                            acceptAll.setOnClickListener(null);
                        } else {
                            acceptAll.setVisibility(View.GONE);
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
                acceptedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                requestedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                rejectedButton.setBackgroundColor(Color.parseColor("#7bc9c1"));//selected
                eventManager.getRejectedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Rejected attendees: " + attendees.toString());
                        acceptAll.setVisibility(View.VISIBLE);
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No rejected attendees found.");
                            acceptAll.setVisibility(View.VISIBLE);
                            acceptAll.setText("No Rejects");
                            acceptAll.setBackgroundColor(Color.parseColor("#ee645f"));
                            acceptAll.setBackgroundTintList(null);
                            acceptAll.setOnClickListener(null);
                        } else {
                            acceptAll.setText("Accept All");
                            acceptAll.setBackgroundColor(Color.parseColor("#829647"));
                            acceptAll.setBackgroundTintList(null);
                            acceptAll.setOnClickListener(v -> {
                                organizer.approveAllEventRequests(event);
                                notifyDataSetChanged();
                            });
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
                acceptedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                requestedButton.setBackgroundColor(Color.parseColor("#7bc9c1"));//selected
                rejectedButton.setBackgroundColor(Color.parseColor("#f3cc91"));
                eventManager.getRequestedAttendees(eventID, new EventManager.AttendeeCallbackList() {
                    @Override
                    public void onSuccess(ArrayList<Attendee> attendees) {
                        Log.d("Events", "Requested attendees: " + attendees.toString());
                        acceptAll.setVisibility(View.VISIBLE);
                        if (attendees.isEmpty()) {
                            Log.d("Events", "No requested attendees found.");
                            acceptAll.setText("No Requests");
                            acceptAll.setBackgroundColor(Color.parseColor("#ee645f"));
                            acceptAll.setBackgroundTintList(null);
                            acceptAll.setOnClickListener(null);
                        } else {
                            acceptAll.setText("Accept All");
                            acceptAll.setBackgroundColor(Color.parseColor("#829647"));
                            acceptAll.setBackgroundTintList(null);
                            acceptAll.setOnClickListener(v -> {
                                organizer.approveAllEventRequests(event);
                                notifyDataSetChanged();
                            });
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

    public void showEventInfo(Event event){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.event_info_page, null);
        dialogBuilder.setView(dialogView);

        TextView eventTitle = dialogView.findViewById(R.id.event_title_eventInfoPage);
        TextView creator = dialogView.findViewById(R.id.creator_eventInfoPage);
        TextView startTime = dialogView.findViewById(R.id.startTime_eventInfoPage);
        TextView endTime = dialogView.findViewById(R.id.endTime_eventInfoPage);
        TextView eventAddress = dialogView.findViewById(R.id.eventAddress_eventInfoPage);
        TextView eventDescription = dialogView.findViewById(R.id.description_eventInfoPage);

        eventTitle.setText(event.getTitle());
        if (event.getCreator() != null){
            creator.setText(event.getCreator().getEmail());
        } else {creator.setText("Unknown Creator");}
        eventAddress.setText(event.getEventAddress());
        eventDescription.setText(event.getDescription());
        startTime.setText(event.getStartTime().toString());
        endTime.setText(event.getEndTime().toString());

        dialogBuilder.setTitle("Event Info");
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

}
