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
import project.group6.eams.utils.InputUtils;

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
        public Button accept_button;
        public Button reject_button;
        public LinearLayout userlist_layout;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmail_userlistlayout);
            userName = itemView.findViewById(R.id.userName_userlistlayout);
            userPhone = itemView.findViewById(R.id.userPhone_userlistlayout);
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
        holder.userPhone.setText(user.getPhoneNumber());
        if(event.getAttendeeStatus(attendees.get(position).getEmail()).equals("approved") || InputUtils.dateHasPassed(event.getStartTime())){
            holder.accept_button.setVisibility(View.GONE);
            holder.reject_button.setVisibility(View.GONE);
        } else if (event.getAttendeeStatus(attendees.get(position).getEmail()).equals("requested")){
            holder.reject_button.setVisibility(View.VISIBLE);
            holder.accept_button.setVisibility(View.VISIBLE);
        } else if (event.getAttendeeStatus(attendees.get(position).getEmail()).equals("rejected")){
            holder.reject_button.setVisibility(View.GONE);
            holder.accept_button.setVisibility(View.VISIBLE);
        }
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context,R.style.dialogtheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.user_info_page, null);
        dialogBuilder.setView(dialogView);

        TextView userEmail = dialogView.findViewById(R.id.userEmail_userInfoPage);
        TextView userPhone = dialogView.findViewById(R.id.userPhone_userInfoPage);
        TextView userName = dialogView.findViewById(R.id.userName_userInfoPage);
        TextView userOrganization = dialogView.findViewById(R.id.userOrganization_userInfoPage);
        TextView userAddress = dialogView.findViewById(R.id.userAddress_userInfoPage);
        TextView userTime = dialogView.findViewById(R.id.userTime_userInfoPage);

        userEmail.setText("Email: "+rUser.getEmail());
        userPhone.setText("Phone Number: "+rUser.getPhoneNumber());
        userName.setText("Name: "+rUser.getFirstname()+ " "+ rUser.getLastname());
        if (user.getUserType().equals("Organizer")) {
            userOrganization.setText("Organization: "+((Organizer)rUser).getOrganizationName());
        } else {
            userOrganization.setText("Attendee");
        }
        userAddress.setText("Address: "+rUser.getAddress());
        if (rUser.getRequestTime()!=null){
            userTime.setText("Request Time: "+rUser.getRequestTime().toDate().toString());
        }
        dialogBuilder.setTitle("User Info");
        AlertDialog b = dialogBuilder.create();
        b.show();

    }
}
