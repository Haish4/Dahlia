package com.example.dahlia.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.Firebase.IncidentsTracking;
import com.example.dahlia.Firebase.PeopleTracking;
import com.example.dahlia.R;
import com.example.dahlia.adapters.ContactRowAdapter;
import com.example.dahlia.adapters.IncidentsTrackingAdapter;
import com.example.dahlia.adapters.PeopleTrackingAdapter;
import com.example.dahlia.databinding.ActivityTrackingBinding;
import com.example.dahlia.databinding.DialogAddMultipleContactsBinding;
import com.example.dahlia.models.EmergencyContact;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PeopleTracking> peopleTrackingList;
    private List<IncidentsTracking> incidentsTrackingList;
    private ActivityTrackingBinding binding;
    private String userId;
    private FirebaseFirestore db;
    private PreferenceManager preferenceManager;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Check for SMS permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }

        // Initialize UI components
        recyclerView = binding.TrackingRecyclerView;
        peopleTrackingList = new ArrayList<>();
        incidentsTrackingList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        userId = preferenceManager.getString(Constants.KEY_USER_ID);

        // Check if userId is null
        if (userId == null) {
            Log.e("TrackingActivity", "User ID is null. Redirecting to login screen.");
            startActivity(new Intent(TrackingActivity.this, SignInActivity.class));
            finish();
            return;
        }

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up UI
        setName();
        fetchPeopleData();
        binding.peopleButton.setOnClickListener(v -> fetchPeopleData());
        binding.incidentsButton.setOnClickListener(v -> fetchIncidentsData());
        binding.backButton3.setOnClickListener(v -> {
            startActivity(new Intent(TrackingActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        binding.sosButton.setOnClickListener(v -> {
            sendSmsToContacts("This is an emergency message.");
            startActivity(new Intent(TrackingActivity.this, SosActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });
        binding.contactList.setOnClickListener(v -> modalList());
    }

    private List<EmergencyContact> retrieveEmergencyContacts() {
        String json = preferenceManager.getString(Constants.KEY_EMERGENCY_CONTACT); // Replace with your actual key
        if (json != null) {
            Type type = new TypeToken<List<EmergencyContact>>() {}.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    private void sendSmsToContacts(String message) {
        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Fetch the user's current location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Format the location into a readable string
                        String userLocation = "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude();
                        String mapsLink = "https://www.google.com/maps?q=" + location.getLatitude() + "," + location.getLongitude();
                        String fullMessage = message + "\nMy current location: " + userLocation + "\nGoogle Maps: " + mapsLink;

                        // Retrieve emergency contacts
                        List<EmergencyContact> contactList = retrieveEmergencyContacts();
                        if (contactList.isEmpty()) {
                            Toast.makeText(this, "No emergency contacts found.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Send SMS to each contact
                        SmsManager smsManager = SmsManager.getDefault();
                        for (EmergencyContact contact : contactList) {
                            try {
                                smsManager.sendTextMessage(contact.getPhoneNumber(), null, fullMessage, null, null);
                                Log.d("SMS", "SMS sent to: " + contact.getName() + " (" + contact.getPhoneNumber() + ")");
                            } catch (Exception e) {
                                Log.e("SMS", "Failed to send SMS to: " + contact.getName() + " (" + contact.getPhoneNumber() + ")", e);
                            }
                        }

                        Toast.makeText(this, "SMS sent to emergency contacts with location.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Unable to fetch location.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LocationError", "Failed to fetch location: " + e.getMessage());
                    Toast.makeText(this, "Failed to fetch location.", Toast.LENGTH_SHORT).show();
                });
    }

    public void modalList() {
        // Initialize the dialog and binding
        Dialog dialog = new Dialog(this);
        DialogAddMultipleContactsBinding dialogBinding = DialogAddMultipleContactsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        // Set up RecyclerView
        ContactRowAdapter adapter = new ContactRowAdapter();
        dialogBinding.recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerViewContacts.setAdapter(adapter);

        // Handle Add New Row button
        dialogBinding.buttonAddContactRow.setOnClickListener(v -> adapter.addContactRow());

        // Handle Save Contacts button
        dialogBinding.buttonSaveContacts.setOnClickListener(v -> {
            List<EmergencyContact> contactList = adapter.getContactList();
            boolean isValid = true;

            for (EmergencyContact contact : contactList) {
                if (contact.getName().isEmpty() || contact.getPhoneNumber().isEmpty()) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                preferenceManager.putEmergencyContacts(contactList); // Save to preferences
                Toast.makeText(this, "Contacts saved successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Enable the My Location layer
        mMap.setMyLocationEnabled(true);

        // Get the last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    private void setName() {
        binding.userName.setText("STAY SAFE " + preferenceManager.getString(Constants.KEY_NAME) + "!");
    }

    private void fetchPeopleData() {
        peopleTrackingList.clear(); // Clear previous data
        db.collection("people")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        PeopleTracking peopleTracking = document.toObject(PeopleTracking.class);
                        peopleTrackingList.add(peopleTracking);
                    }
                    adapter = new PeopleTrackingAdapter(peopleTrackingList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("FetchPeopleData", "Error fetching people data: " + e.getMessage());
                });
    }

    private void fetchIncidentsData() {
        incidentsTrackingList.clear(); // Clear previous data
        db.collection("incidents")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        IncidentsTracking incidentsTracking = document.toObject(IncidentsTracking.class);
                        incidentsTrackingList.add(incidentsTracking);
                    }
                    adapter = new IncidentsTrackingAdapter(incidentsTrackingList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("FetchIncidentsData", "Error fetching incidents data: " + e.getMessage());
                });
    }
}