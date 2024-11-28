package project.group6.eams.activityUtils;

import android.content.Context;
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

        holder.accept_button.setOnClickListener(v -> {
            Administrator.approveRequest(user.getEmail());
            users.remove(position);
            notifyDataSetChanged();
        });
        holder.reject_button.setOnClickListener(v -> {
            Administrator.rejectRequest(user.getEmail());
            users.remove(position);
            notifyDataSetChanged();
        });
        holder.userlist_layout.setOnLongClickListener(v->{
            showUserInfo(user);
            return true;
        });
        if (user.getRejectionStatus()){
            holder.reject_button.setVisibility(View.GONE);
        }
        if (!user.getRejectionStatus()){
            holder.reject_button.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount () {
        return users.size();
    }

    public void showUserInfo(User user){
        RegisterableUser rUser = (RegisterableUser)user;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.dialogtheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.user_info_page, null);
        dialogBuilder.setView(dialogView);

        TextView userEmail = dialogView.findViewById(R.id.userEmail_userInfoPage);
        TextView userPhone = dialogView.findViewById(R.id.userPhone_userInfoPage);
        TextView userName = dialogView.findViewById(R.id.userName_userInfoPage);
        TextView userOrganization = dialogView.findViewById(R.id.userOrganization_userInfoPage);
        TextView userAddress = dialogView.findViewById(R.id.userAddress_userInfoPage);
        TextView userTime = dialogView.findViewById(R.id.userTime_userInfoPage);

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
