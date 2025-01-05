package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dahlia.Firebase.FeedItem;
import com.example.dahlia.R;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private final List<FeedItem> feedItems;

    public FeedAdapter(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem feedItem = feedItems.get(position);
        Glide.with(holder.itemView)
                .load(feedItem.getImageUrl())
                .into(holder.imageView);
        holder.titleText.setText(feedItem.getTitle());
        holder.feedInputText.setText(feedItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView titleText;
        public TextView feedInputText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProfile01);
            titleText = itemView.findViewById(R.id.titleText01);
            feedInputText = itemView.findViewById(R.id.feedInputText01);
        }
    }
}
