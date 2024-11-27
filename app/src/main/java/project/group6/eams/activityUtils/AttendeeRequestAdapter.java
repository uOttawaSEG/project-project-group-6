package project.group6.eams.activityUtils;

import android.content.Context;
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
import project.group6.eams.users.RegisterableUser;
import project.group6.eams.users.User;
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
    private Context context;

    public AttendeeRequestAdapter (ArrayList<Attendee> attendees, Event event, Organizer organizer, Context context) {
        Log.d("Event","AttendeeRequestAdapter has received: "+attendees.toString());
        this.attendees = attendees;
        this.event = event;
        this.organizer = organizer;
        this.context = context;
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
        public LinearLayout userlist_layout;

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
            userlist_layout = itemView.findViewById(R.id.userlist_layout);
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
        holder.userOrganization.setText("Attendee");

        holder.accept_button.setOnClickListener(v -> {
            organizer.approveEventRequest(event,user.getEmail());
            attendees.remove(position);
            notifyDataSetChanged();
        });

        holder.reject_button.setOnClickListener(v -> {
            organizer.rejectEventRequest(event,user.getEmail());
            attendees.remove(position);
            notifyDataSetChanged();
        });
        holder.userlist_layout.setOnLongClickListener(v->{
            showUserInfo(user);
            return true;
        });

    }

    @Override
    public int getItemCount () {
        return attendees.size();
    }

    public void showUserInfo(User user){
        RegisterableUser rUser = (RegisterableUser)user;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.user_info_page, null);
        dialogBuilder.setView(dialogView);

        TextView userEmail = dialogView.findViewById(R.id.userEmail_userlistlayout);
        TextView userPhone = dialogView.findViewById(R.id.userPhone_userlistlayout);
        TextView userName = dialogView.findViewById(R.id.userName_userlistlayout);
        TextView userOrganization = dialogView.findViewById(R.id.userOrganization_userlistlayout);
        TextView userAddress = dialogView.findViewById(R.id.userAddress_userlistlayout);
        TextView userTime = dialogView.findViewById(R.id.userTime_userlistlayout);

        userEmail.setText(rUser.getEmail());
        userPhone.setText(rUser.getPhoneNumber());
        userName.setText(rUser.getFirstname()+ " "+ rUser.getLastname());
        if (user.getUserType().equals("Organizer")) {
            userOrganization.setText(((Organizer)rUser).getOrganizationName());
        } else {
            userOrganization.setText("Attendee");
        }
        userAddress.setText(rUser.getAddress());
        if (rUser.getRequestTime()!=null){
            userTime.setText(rUser.getRequestTime().toDate().toString());
        }
        dialogBuilder.setTitle("User Info");
        AlertDialog b = dialogBuilder.create();
        b.show();

    }
}
