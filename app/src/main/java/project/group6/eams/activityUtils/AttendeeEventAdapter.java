package project.group6.eams.activityUtils;



import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import project.group6.eams.R;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.EventManager;
import project.group6.eams.utils.InputUtils;

public class AttendeeEventAdapter extends RecyclerView.Adapter<AttendeeEventAdapter.ViewHolder> {
    private final ArrayList<Event> events;
    private ArrayList<Event> eventsFiltered;
    private Attendee attendee = (Attendee)AppInfo.getInstance().getCurrentUser();


    public AttendeeEventAdapter (ArrayList<Event> events) {
        this.events = events; // final, unchangable, to reset when filter is empty
        eventsFiltered = new ArrayList<Event>(); // for filteration
        eventsFiltered.addAll(events);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView eventAddress;
        public TextView eventDescription;
        public Button requestToAttendButton;
        public Button delete;
        public boolean requested;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_eventlistlayout);
            creator = itemView.findViewById(R.id.creator_eventlistlayout);
            startTime = itemView.findViewById(R.id.startTime_eventlistlayout);
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
        Event event = eventsFiltered.get(position);
        holder.eventTitle.setText(event.getTitle());
        if (event.getCreator() != null){
            holder.creator.setText(event.getCreator().getEmail());
        } else {holder.creator.setText("Unknown Creator");}
        holder.eventAddress.setText(event.getEventAddress());
        holder.eventDescription.setText(event.getDescription());
        holder.startTime.setText(event.getStartTime().toString()+"-"+event.getEndTime().toString());
        if (event.isRegistered(attendee.getEmail())){
            holder.requestToAttendButton.setText("Cancel Request");
            holder.requestToAttendButton.setOnClickListener(v -> {
                //Checks to see if the event is not within 24 hours and the attendee status is requested
                //(i.e. the attendee has not been accepted or rejected
                if (!(event.getStartTime().before(InputUtils.add(new Date(), Calendar.HOUR,24))) &&
                        event.getAttendeeStatus(attendee.getEmail()).equals("requested")){
                    attendee.cancelRegistration(event);
                    eventsFiltered.remove(position);
                    notifyDataSetChanged();
                } else if (event.getAttendeeStatus(attendee.getEmail()).equals("rejected")){
                    ActivityUtils.showToast("Event has already been rejected",holder.itemView.getContext());
                } else if (event.getAttendeeStatus(attendee.getEmail()).equals("approved")){
                    ActivityUtils.showToast("Event has already been approved",holder.itemView.getContext());
                } else {
                    ActivityUtils.showToast("Event is within 24 hours", holder.itemView.getContext());
                }
            });
        } else {
            holder.requestToAttendButton.setText("Request");
            holder.requestToAttendButton.setOnClickListener(v -> {
                EventManager em = new EventManager("Events");
                em.getRequestedEvents(attendee.getEmail(), new EventManager.EventCallbackList(){
                    @Override
                    public void onSuccess (ArrayList<Event> events) {
                        if (!attendee.hasEventConflict(events,event)){
                            attendee.requestToRegister(event);
                            eventsFiltered.remove(holder.getAdapterPosition());
                            if (event.getAutomaticApproval()){
                                Organizer eventCreator = event.getCreator();
                                eventCreator.approveAllEventRequests(event);
                            }
                            notifyDataSetChanged();
                        } else {
                            Log.d("Event","Event conflict detected");
                            holder.requestToAttendButton.setText("Event Conflict");
                            holder.requestToAttendButton.setBackgroundColor(Color.parseColor("#ee645f"));
                            holder.requestToAttendButton.setBackgroundTintList(null);
                            holder.requestToAttendButton.setOnClickListener(null);

                        }
                    }
                    @Override
                    public void onError (Exception e) {
                        Log.e("Database", Objects.requireNonNull(e.getMessage()));
                    }
                });
            });
        }
        String status = event.getAttendeeStatus(attendee.getEmail());
        if (status!=null){
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setText(event.getAttendeeStatus(attendee.getEmail()));
            switch (status){
                case "approved":
                    holder.delete.setBackgroundColor(Color.parseColor("#829647"));
                    holder.delete.setBackgroundTintList(null);
                    break;
                case "requested":
                    holder.delete.setBackgroundColor(Color.parseColor("#dea22c"));
                    holder.delete.setBackgroundTintList(null);
                    break;
                case "rejected":
                    holder.delete.setBackgroundColor(Color.parseColor("#ee645f"));
                    holder.delete.setBackgroundTintList(null);
                    break;
            }

        } else {
            holder.delete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
        return eventsFiltered.size();
    }

    /**
     * custom filtration based on Event attributes (title, description).
     *<a href = "https://abhiandroid.com/ui/searchview#gsc.tab=0" </a>
     * @param query is the search query entered by the user
     */
    public void filter (String query) {
        ArrayList<Event> filteredList = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(events);
        } else {
            for (Event event : events) {
                if (event.getTitle().toLowerCase().contains(query.toLowerCase()) || event.getDescription().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(event);
                }
            }
        }
        eventsFiltered.clear();
        eventsFiltered.addAll(filteredList);
        notifyDataSetChanged(); // updates adapter with new filtered data
    }

}
