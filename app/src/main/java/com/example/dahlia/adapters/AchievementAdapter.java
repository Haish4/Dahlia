package com.example.dahlia.adapters;

import static android.opengl.ETC1.decodeImage;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.Firebase.FeedItem;
import com.example.dahlia.databinding.ItemAchivementBinding;
import com.example.dahlia.databinding.ItemContainerSentMessageBinding;
import com.example.dahlia.models.Achievement;
import com.example.dahlia.models.ChatMessage;
import com.example.dahlia.utilities.Constants;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>{

    public interface OnItemClickListener {
        void onItemClick(Achievement achievement);
    }
    List<Achievement> Achievements;
    private final OnItemClickListener onItemClickListener;


    public AchievementAdapter(List<Achievement> Achievements, OnItemClickListener listener) {
        this.Achievements = Achievements;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AchievementViewHolder(
                ItemAchivementBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = Achievements.get(position);
        holder.binding.title.setText(achievement.getTitle());
        holder.binding.image.setImageBitmap(decodeImage(achievement.getImage()));
        holder.binding.achievementContainer.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(achievement);
            }
        });
    }


    @Override
    public int getItemCount() {
        return Achievements.size();
    }
    Bitmap decodeImage(String encoded){
        byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {

        private final ItemAchivementBinding binding;
        public AchievementViewHolder(ItemAchivementBinding itemAchivementBinding) {
            super(itemAchivementBinding.getRoot());
            binding = itemAchivementBinding;
        }

        void setData(Achievement achievement){
            binding.image.setImageBitmap(decodeImage(achievement.getImage()));
            binding.title.setText(achievement.getTitle());
        }

        Bitmap decodeImage(String encoded){
            byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }
}
