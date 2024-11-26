package project.group6.eams.activityUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;
import project.group6.eams.users.Attendee;
import project.group6.eams.utils.Event;
import project.group6.eams.users.Organizer;

/**
 * Adapter for the Organizer Page that shows the attendees that have requested to attend an event.
 */
public class AttendeeRequestAdapter extends RecyclerView.Adapter<AttendeeRequestAdapter.ViewHolder> {
    private final ArrayList<Attendee> attendees;
    //The even this list is attached to
    private final Event event;
    private final Organizer organizer;

    public AttendeeRequestAdapter (ArrayList<Attendee> attendees, Event event, Organizer organizer) {
        Log.d("Event","AttendeeRequestAdapter has received: "+attendees.toString());
        this.attendees = attendees;
        this.event = event;
        this.organizer = organizer;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userEmail;
        public TextView userName;
        public TextView userPhone;
        public TextView userOrganization;
        public TextView userTime;
        public TextView userAddress;
        public Button accept_button;
        public Button reject_button;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmail_userlistlayout);
            userName = itemView.findViewById(R.id.userName_userlistlayout);
            userPhone = itemView.findViewById(R.id.userPhone_userlistlayout);
            userOrganization = itemView.findViewById(R.id.userOrganization_userlistlayout);
            userTime = itemView.findViewById(R.id.userTime_userlistlayout);
            userAddress = itemView.findViewById(R.id.userAddress_userlistlayout);
            accept_button = itemView.findViewById(R.id.accept_button_userlistlayout);
            reject_button = itemView.findViewById(R.id.reject_button_userlistlayout);
        }
    }

    @NonNull
    @Override
    public AttendeeRequestAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull AttendeeRequestAdapter.ViewHolder holder, int position) {
        Attendee user =  attendees.get(position);
        holder.userEmail.setText(user.getEmail());
        holder.userName.setText((user.getFirstname() + " " + user.getLastname()));
        holder.userPhone.setText(user.getPhoneNumber());
        holder.userTime.setText(user.getRequestTime().toDate().toString());
        holder.userAddress.setText(user.getAddress());
        holder.userOrganization.setText("Attendee");

        holder.accept_button.setOnClickListener(v -> organizer.approveEventRequest(event,user.getEmail()));
        holder.reject_button.setOnClickListener(v -> organizer.rejectEventRequest(event,user.getEmail()));

    }

    @Override
    public int getItemCount () {
        return attendees.size();
    }
}
