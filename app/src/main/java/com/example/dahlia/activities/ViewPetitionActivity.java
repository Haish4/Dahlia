package com.example.dahlia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dahlia.R;
import com.example.dahlia.adapters.PetitionAdapter;
import com.example.dahlia.models.Petition;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class ViewPetitionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PetitionAdapter petitionAdapter, petitionAdapter2;
    private List<Petition> petitionList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_petition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //backButton
        findViewById(R.id.backButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPetitionActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerViewPetitions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        petitionList = new ArrayList<>();
        petitionAdapter = new PetitionAdapter(petitionList, petition -> {
            Intent intent = new Intent(ViewPetitionActivity.this, PetitionItemDetails.class);
            intent.putExtra("topic", petition.getTopic());
            intent.putExtra("details", petition.getDetails());
            intent.putExtra("minSigned", petition.getMinSigned());
            intent.putExtra("dateEnds", petition.getDateEnds());
            intent.putExtra("type", petition.getType());
            startActivity(intent);
        });
        recyclerView.setAdapter(petitionAdapter);

        db = FirebaseFirestore.getInstance();

        loadPetitionsFromFirestore();

    }

    private void loadPetitionsFromFirestore() {
        // Firestore query to fetch petitions
        db.collection("petition")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (var doc : task.getResult()) {
                            Petition petition = new Petition(
                                    doc.getString("topic"),
                                    doc.getString("details"),
                                    doc.getLong("minSigned").intValue(),
                                    doc.getString("endDate"),
                                    doc.getString("type")
                            );
                            petitionList.add(petition);
                        }

                        //Notify adapter that data has changed
                        petitionAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }


    /*private void loadPetitionsFromFirestore() {
        CollectionReference petitionRef = db.collection("petition");

        //Real-time listener for updates
        petitionRef.addSnapShotListener((QuerySnapshot value, FirebaseFirestoreException error) -> {
            if (error != null) {
                Log.e(TAG, "Firestore error: ", error);
                return;
            }

            petitionList.clear();

            if (value != null) {
                for (var doc : value.getDocuments()) {
                    String topic = doc.getString("topic");
                    String details = doc.getString("details");
                    int minSigned = doc.getLong("minSigned").intValue();
                    String dateEnds = doc.getString("dateEnds");
                    String type = doc.getString("type");

                    petitionList.add(new Petition(topic, details, minSigned, dateEnds, type));
                }
                petitionAdapter.notifyDataSetChanged();
            }
        });


    }*/


}