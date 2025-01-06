package com.example.dahlia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.R;
import com.example.dahlia.models.Petition;

import java.util.List;

public class PetitionAdapter extends RecyclerView.Adapter<PetitionAdapter.PetitionViewHolder> {

    //private Context context;
    private List<Petition> petitionList;
    private OnItemClickListener listener;

    //Constructor
    public PetitionAdapter(List<Petition> petitionList, OnItemClickListener listener) {
        this.petitionList = petitionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PetitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.petition_item, parent, false);
        return new PetitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionViewHolder holder, int position) {
        Petition petition = petitionList.get(position);
        holder.tvTopicTitle.setText(petition.getTopic());
        holder.tvType.setText(petition.getType());

        //handle item click
        holder.itemView.setOnClickListener(v -> listener.onItemClick(petition));

    }

    @Override
    public int getItemCount() {
        return petitionList.size();
    }

    public static class PetitionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicTitle, tvType;// tvTopicSnippet;

        public PetitionViewHolder(View itemView) {
            super(itemView);
            tvTopicTitle = itemView.findViewById(R.id.tvTopicTitle);
            tvType = itemView.findViewById(R.id.tvType);
            //tvTopicSnippet = itemView.findViewById(R.id.tvTopicSnippet);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Petition petition);
    }
}
