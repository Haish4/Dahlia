package com.example.dahlia.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dahlia.Firebase.IncidentsTracking;
import com.example.dahlia.R;
import com.example.dahlia.adapters.IncidentsTrackingAdapter;
import com.example.dahlia.databinding.ActivitySosBinding;
import com.example.dahlia.models.EmergencyContact;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SosActivity extends AppCompatActivity {

    ActivitySosBinding binding;
    PreferenceManager preferenceManager;
    private List<IncidentsTracking> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySosBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setBinding();
        fetchIncidentsData();

    }

    private void fetchIncidentsData() {
        contactList.clear(); // Clear previous data
        List<EmergencyContact> list = new ArrayList<>();
        list = preferenceManager.getEmergencyContacts();

        for(EmergencyContact c : list){
            String name = c.getName();
            String no = c.getPhoneNumber();
            contactList.add(new IncidentsTracking(name, no));
        }

        binding.TrackingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        IncidentsTrackingAdapter adapter = new IncidentsTrackingAdapter(contactList);
        binding.TrackingRecyclerView.setAdapter(adapter);

    }
    private void setBinding(){
        binding.backButton3.setOnClickListener(v -> onBackPressed());
        binding.sosButton.setOnClickListener(v -> onBackPressed());
        binding.userName.setText("STAY SAFE " + preferenceManager.getString(Constants.KEY_NAME));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}