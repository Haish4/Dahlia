package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.R;
import com.example.dahlia.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public List<Event> eventList;

    public EventAdapter(List<Event> evenList) {
        this.eventList = evenList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false); // Custom item layout
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.dateTextView.setText(event.getDate());
        holder.eventNameTextView.setText(event.getEventName());
        holder.eventDetailsTextView.setText(event.getEventDetails());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, eventNameTextView, eventDetailsTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            eventDetailsTextView = itemView.findViewById(R.id.eventDetailsTextView);
        }
    }
}
