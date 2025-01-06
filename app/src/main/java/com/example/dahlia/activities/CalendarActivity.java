package com.example.dahlia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.R;
import com.example.dahlia.adapters.EventAdapter;
import com.example.dahlia.models.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends BaseActivity {

    private CalendarView calendarView;
    private EditText eventName, eventDetails;
    private Button addButton;
    private String selectedDate = ""; // To store selected date
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //backButton
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finish();
            }
        });

        calendarView = findViewById(R.id.calendarView);
        eventName = findViewById(R.id.editTextText);
        eventDetails = findViewById(R.id.editTextText2);
        addButton = findViewById(R.id.button);

        db = FirebaseFirestore.getInstance();

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        addButton.setOnClickListener(v ->
            addEventToFirestore());

//steps to get data from firestore
        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        loadEventsFromFirestore();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void addEventToFirestore() {
        String name = eventName.getText().toString();
        String details = eventDetails.getText().toString();

        if(selectedDate.isEmpty() || name.isEmpty() || details.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //create event object
        HashMap<String, Object> event = new HashMap<>();
        event.put("name", name);
        event.put("details", details);
        event.put("date", selectedDate);

        //add event to firestore
        db.collection("event")
                .add(event)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    eventName.setText("");
                    eventDetails.setText("");
                    loadEventsFromFirestore();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadEventsFromFirestore() {
        db.collection("event")
                //.whereEqualTo("date", selectedDate)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String date = document.getString("date");
                        String name = document.getString("name");
                        String details = document.getString("details");
                        eventList.add(new Event(date, name, details));
                    }
                    eventAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
                });
    }
}
