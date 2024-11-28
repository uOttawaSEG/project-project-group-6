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
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import project.group6.eams.R;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.Organizer;
import project.group6.eams.utils.AppInfo;
import project.group6.eams.utils.Event;


public class AttendeeEventAdapter extends RecyclerView.Adapter<AttendeeEventAdapter.ViewHolder> {
    private final ArrayList<Event> events;
    private ArrayList<Event> eventsFiltered;
    private Attendee attendee = (Attendee)AppInfo.getInstance().getCurrentUser();
    private Context context;


    public AttendeeEventAdapter (ArrayList<Event> events, Context context) {
        this.events = events; // final, unchangable, to reset when filter is empty
        eventsFiltered = new ArrayList<Event>(); // for filteration
        eventsFiltered.addAll(events);
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView endTime;
        public Button requestToAttendButton;
        public Button delete;
        public boolean requested;
        public LinearLayout eventlist_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title_eventlistlayout);
            creator = itemView.findViewById(R.id.creator_eventlistlayout);
            startTime = itemView.findViewById(R.id.startTime_eventlistlayout);
            endTime = itemView.findViewById(R.id.endTime_eventlistlayout);
            requestToAttendButton = itemView.findViewById(R.id.attendee_list_button_eventlistlayout);
            delete = itemView.findViewById(R.id.delete_event_eventlistlayout);
            eventlist_layout = itemView.findViewById(R.id.eventlist_layout);

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
        holder.startTime.setText(event.getStartTime().toString());
        holder.endTime.setText(event.getEndTime().toString());
        if (event.isRegistered(attendee.getEmail())){
            holder.requestToAttendButton.setText("Cancel Request");
            holder.requestToAttendButton.setOnClickListener(v -> {
                attendee.cancelRegistration(event);
                eventsFiltered.remove(position);
                notifyDataSetChanged();
            });
        } else {
            holder.requestToAttendButton.setText("Request");
            holder.requestToAttendButton.setOnClickListener(v -> {
                attendee.requestToRegister(event);
                eventsFiltered.remove(position);
                if (event.getAutomaticApproval()){
                    Organizer eventCreator = event.getCreator();
                    eventCreator.approveAllEventRequests(event);
                }
                notifyDataSetChanged();
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
        holder.eventlist_layout.setOnLongClickListener(v->{
            showEventInfo(event);
            return true;
        });

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
