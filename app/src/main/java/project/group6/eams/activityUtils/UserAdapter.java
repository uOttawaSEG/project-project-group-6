package project.group6.eams.activityUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;
import project.group6.eams.users.Administrator;
import project.group6.eams.users.Organizer;
import project.group6.eams.users.RegisterableUser;
import project.group6.eams.users.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> users;
    private Context context;

    public UserAdapter (ArrayList<User> users, Context context) {
        this.users = users;
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
        public RecyclerView attendee_recycler_view;

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
            attendee_recycler_view = itemView.findViewById(R.id.attendee_recycler_view_event_attendees_organizer);
        }
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_layout,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull UserAdapter.ViewHolder holder, int position) {
        RegisterableUser user = (RegisterableUser) users.get(position);
        holder.userEmail.setText(user.getEmail());
        holder.userName.setText((user.getFirstname() + " " + user.getLastname()));
        holder.userPhone.setText(user.getPhoneNumber());
        if (user.getRequestTime() != null){
            holder.userTime.setText(user.getRequestTime().toDate().toString());
        }
        holder.userAddress.setText(user.getAddress());
        if (user.getUserType().equals("Organizer")) {
            holder.userOrganization.setText(((Organizer) user).getOrganizationName());
        } else {
            holder.userOrganization.setText("Attendee");
        }

        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Administrator.approveRequest(user.getEmail());
            }
        });
        holder.reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Administrator.rejectRequest(user.getEmail());
            }
        });
        holder.attendee_recycler_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showUserInfo(user);
                return true;
            }
        });

    }

    @Override
    public int getItemCount () {
        return users.size();
    }

    public void showUserInfo(User user){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.user_info_page, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("User Info");
        AlertDialog b = dialogBuilder.create();
        b.show();

    }
}
