package com.example.dahlia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.Firebase.IncidentsTracking;
import com.example.dahlia.Firebase.PeopleTracking;
import com.example.dahlia.adapters.IncidentsTrackingAdapter;
import com.example.dahlia.adapters.PeopleTrackingAdapter;
import com.example.dahlia.R;
import com.example.dahlia.databinding.ActivityTrackingBinding;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TrackingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PeopleTracking> peopleTrackingList;
    private List<IncidentsTracking> incidentsTrackingList;
    private ImageButton peopleButton, incidentsButton;
    private ActivityTrackingBinding binding;
    private String userId;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.TrackingRecyclerView;
        peopleButton = binding.peopleButton;
        incidentsButton = binding.incidentsButton;

        // Set listener for the back button
        binding.backButton3.setOnClickListener(v -> {
            startActivity(new Intent(TrackingActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        peopleTrackingList = new ArrayList<>();
        incidentsTrackingList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());

        userId = preferenceManager.getString(Constants.KEY_USER_ID);

        if (userId != null) {
            fetchUserName(); // Fetch and display the user's name
        } else {
            binding.userName.setText("Unknown User");
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchUserName();

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPeopleData();
            }
        });

        incidentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchIncidentsData();
            }
        });

        setListener();
    }

    private void fetchUserName() {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        binding.userName.setText(userName);
                    } else {
                        // Handle case where user document doesn't exist
                        binding.userName.setText("Unknown User");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    binding.userName.setText("Error loading username");
                });
    }

    private void fetchPeopleData() {
        peopleTrackingList.clear(); // Clear previous data
        db.collection("people")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            PeopleTracking peopleTracking = document.toObject(PeopleTracking.class);
                            peopleTrackingList.add(peopleTracking);
                        }
                        adapter = new PeopleTrackingAdapter(peopleTrackingList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });
    }

    private void fetchIncidentsData() {
        incidentsTrackingList.clear(); // Clear previous data
        db.collection("incidents")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            IncidentsTracking incidentsTracking = document.toObject(IncidentsTracking.class);
                            incidentsTrackingList.add(incidentsTracking);
                        }
                        adapter = new IncidentsTrackingAdapter(incidentsTrackingList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });
    }

    private void setListener() {
        binding.backButton3.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

}