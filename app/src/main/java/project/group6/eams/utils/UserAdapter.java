package project.group6.eams.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import project.group6.eams.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> users;

    public UserAdapter(ArrayList<User> users){
        this.users = users;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView userEmail;
        public TextView userName;
        public TextView userPhone;
        public TextView userOrganization;
        public TextView userTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmail);
            userName = itemView.findViewById(R.id.userName);
            userPhone = itemView.findViewById(R.id.userPhone);
            userOrganization = itemView.findViewById(R.id.userOrganization);
            userTime = itemView.findViewById(R.id.userTime);
        }
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        RegisterableUser user = (RegisterableUser) users.get(position);
        holder.userEmail.setText(user.getEmail());
        holder.userName.setText((user.getFirstname() + " " + user.getLastname()));
        holder.userPhone.setText(user.getPhoneNumber());
        holder.userTime.setText(user.getRequestTime().toDate().toString());
        if (user.userType.equals("Organizer")){
            holder.userTime.setText(((Organizer) user).getOrganizationName());
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
