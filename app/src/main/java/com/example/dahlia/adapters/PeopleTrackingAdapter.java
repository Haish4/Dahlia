package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dahlia.Firebase.PeopleTracking;
import com.example.dahlia.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class PeopleTrackingAdapter extends RecyclerView.Adapter<PeopleTrackingAdapter.ViewHolder> {

    private List<PeopleTracking> peopleTrackingList;

    public PeopleTrackingAdapter(List<PeopleTracking> nestedItemList) {
        this.peopleTrackingList = nestedItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_people_tracking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PeopleTracking item = peopleTrackingList.get(position);

        holder.nameText.setText(item.getName());
        holder.locationText.setText(item.getLocation());

        if (item.getImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .into(holder.imageProfile2);
        }
    }

    @Override
    public int getItemCount() {
        return peopleTrackingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, locationText;
        RoundedImageView imageProfile2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            locationText = itemView.findViewById(R.id.locationText);
            imageProfile2 = itemView.findViewById(R.id.imageProfile2);
        }
    }
}
