package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.Firebase.IncidentsTracking;
import com.example.dahlia.R;

import java.util.List;

public class IncidentsTrackingAdapter extends RecyclerView.Adapter<IncidentsTrackingAdapter.ViewHolder> {

        private List<IncidentsTracking> incidentsList;

        public IncidentsTrackingAdapter(List<IncidentsTracking> incidentsList) {
            this.incidentsList = incidentsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidents_tracking, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            IncidentsTracking incident = incidentsList.get(position);

            holder.incidentsNameText.setText(incident.getName());
            holder.incidentsLocationText.setText(incident.getLocation());
        }

        @Override
        public int getItemCount() {
            return incidentsList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView incidentsNameText, incidentsLocationText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                incidentsNameText = itemView.findViewById(R.id.incidentsNameText);
                incidentsLocationText = itemView.findViewById(R.id.incidentsLocationText);
            }
        }

}
