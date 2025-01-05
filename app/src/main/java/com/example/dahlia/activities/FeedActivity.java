package com.example.dahlia.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dahlia.Firebase.FeedItem;
import com.example.dahlia.adapters.FeedAdapter;
import com.example.dahlia.databinding.ActivityFeedBinding;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private FeedAdapter adapter;
    private List<FeedItem> feedItems;
    private FirebaseFirestore db;
    private ActivityFeedBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        // Initialize View Binding
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedItems = new ArrayList<>();
        adapter = new FeedAdapter(feedItems);
        binding.recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch Data and Set Listeners
        fetchDataFromFirestore();
        setListener();
    }


    private void setListener() {
        binding.backButton3.setOnClickListener(v -> {
            finish(); // Go back to the previous activity
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        binding.addFeedButton.setOnClickListener(v -> {
            FeedPostForm dialog = new FeedPostForm();
            String image = preferenceManager.getString(Constants.KEY_IMAGE);
            dialog.setListener((title, description) -> {
                FeedItem feedItem = new FeedItem(image, title, description);
                db.collection("feed")
                        .add(feedItem)
                        .addOnSuccessListener(documentReference -> {

                            feedItems.add(feedItem);
                            adapter.notifyItemInserted(feedItems.size() - 1); // Notify adapter of new item
                            Toast.makeText(FeedActivity.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(FeedActivity.this, "Failed to add post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });


        });

            // Show the dialog
            dialog.show(getSupportFragmentManager(), "FeedPostForm");
        });

    }

    private void fetchDataFromFirestore() {
        CollectionReference feedRef = db.collection("feed");

        feedRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Log.d("TAG", "Data fetched successfully! Document count: " + queryDocumentSnapshots.size());
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("TAG", "Document ID: " + document.getId() + " | Data: " + document.getData());
                            FeedItem feedItem = document.toObject(FeedItem.class);
                            if (feedItem != null) {
                                feedItems.add(feedItem);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Update RecyclerView
                        Log.d("TAG", "RecyclerView updated with " + feedItems.size() + " items.");
                    } else {
                        Log.d("TAG", "No documents found in the feed collection.");
                        Toast.makeText(this, "No data available in Firebase.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error fetching data from Firebase", e);
                    Toast.makeText(this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                });
    }

}
