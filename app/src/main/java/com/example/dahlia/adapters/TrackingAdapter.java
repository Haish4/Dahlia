package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.R;

import java.util.List;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder> {

        private List<String> itemList;

        public TrackingAdapter(List<String> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public TrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_tracking, parent, false);
            return new TrackingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrackingViewHolder holder, int position) {
            String item = itemList.get(position);
            holder.textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public static class TrackingViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public TrackingViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
}
