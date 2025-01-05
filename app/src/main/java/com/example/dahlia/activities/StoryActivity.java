package com.example.dahlia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dahlia.databinding.ActivityStoryBinding;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoryActivity extends AppCompatActivity {

    private ActivityStoryBinding binding;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialiseDatabase();
        setContentContext();
        setListener();
    }

    public void initialiseDatabase(){
        database= FirebaseFirestore.getInstance();
        preferenceManager=new PreferenceManager(getApplicationContext());
    }

    public void setContentContext() {
        database.collection("story")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot document : task.getResult()) {
                            binding.TVName.setText(document.getString("name"));
                            binding.TVProfession.setText(document.getString("profession"));
                            binding.TVStoryDetails.setText(document.getString("storydetail"));
                        }
                    } else {
                        Log.w("Firestore", "Error fetching document", task.getException());
                    }
                });
    }

    public void setListener() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

}
