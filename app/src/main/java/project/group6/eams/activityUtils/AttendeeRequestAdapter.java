package project.group6.eams.activityUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;
import project.group6.eams.utils.Administrator;
import project.group6.eams.utils.Attendee;
import project.group6.eams.utils.Event;
import project.group6.eams.utils.Organizer;
import project.group6.eams.utils.RegisterableUser;
import project.group6.eams.utils.User;

public class AttendeeRequestAdapter extends RecyclerView.Adapter<AttendeeRequestAdapter.ViewHolder> {
    private ArrayList<Attendee> attendees;
    //The even this list is attached to
    private Event event;
    private Organizer organizer;

    public AttendeeRequestAdapter (ArrayList<Attendee> attendees, Event event, Organizer organizer) {
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
            userEmail = itemView.findViewById(R.id.userEmail);
            userName = itemView.findViewById(R.id.userName);
            userPhone = itemView.findViewById(R.id.userPhone);
            userOrganization = itemView.findViewById(R.id.userOrganization);
            userTime = itemView.findViewById(R.id.userTime);
            userAddress = itemView.findViewById(R.id.userAddress);
            accept_button = itemView.findViewById(R.id.accept_button);
            reject_button = itemView.findViewById(R.id.reject_button);
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


        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                organizer.approveEventRequest(event,user.getEmail());
            }
        });
        holder.reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                organizer.rejectEventRequest(event,user.getEmail());
            }
        });

    }

    @Override
    public int getItemCount () {
        return attendees.size();
    }
}
