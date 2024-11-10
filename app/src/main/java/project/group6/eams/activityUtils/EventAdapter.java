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
import project.group6.eams.utils.Event;
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> events;
    private boolean isOnPastEventPage;

    public EventAdapter (ArrayList<Event> events,boolean isOnPastEventPage) {
        this.events = events;
        this.isOnPastEventPage = isOnPastEventPage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle;
        public TextView creator;
        public TextView startTime;
        public TextView endTime;
        public TextView eventAddress;
        public TextView eventDescription;
        public Button attendee_list_button;
        public Button accept_all;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title);
            creator = itemView.findViewById(R.id.creator);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            eventAddress = itemView.findViewById(R.id.eventAddress);
            eventDescription = itemView.findViewById(R.id.description);
            attendee_list_button = itemView.findViewById(R.id.attendee_list_button);
            accept_all = itemView.findViewById(R.id.accept_all);
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
        Event event = (Event) events.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.creator.setText(event.getCreator().getEmail());
        holder.eventAddress.setText(event.getEventAddress());
        holder.eventDescription.setText(event.getDescription());
        holder.startTime.setText(event.getStartTime().toString());
        holder.endTime.setText(event.getEndTime().toString());


    }
    @Override
    public int getItemCount () {
        return 0;
    }
}
