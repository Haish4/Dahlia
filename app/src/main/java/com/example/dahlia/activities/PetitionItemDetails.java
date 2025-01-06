package com.example.dahlia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahlia.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetitionItemDetails extends AppCompatActivity {

    private TextView tvPetitionTopic, tvPetitionDetails, tvMinimumSigned, tvDateEnds, tvPetitionType, tvCount;
    private Button btnSignPetition;
    private int currentCount;
    private String petitionId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_petition_item_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //backButton
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetitionItemDetails.this, ViewPetitionActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        //home
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetitionItemDetails.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        tvPetitionTopic = findViewById(R.id.tvPetitionTopic);
        tvPetitionDetails = findViewById(R.id.tvPetitionDetails);
        tvMinimumSigned = findViewById(R.id.tvMinimumSigned);
        tvDateEnds = findViewById(R.id.tvDateEnds);
        tvPetitionType = findViewById(R.id.tvPetitionType);
        tvCount = findViewById(R.id.tvCount);
        btnSignPetition = findViewById(R.id.btnSignPetition);


        // Get data from Intent
        Intent intent = getIntent();
        String topic = intent.getStringExtra("topic");
        String details = intent.getStringExtra("details");
        int minSigned = intent.getIntExtra("minSigned", 0);
        String dateEnds = intent.getStringExtra("endDate");
        String type = intent.getStringExtra("type");
        //petitionId = intent.getStringExtra("petitionId");
        currentCount = intent.getIntExtra("count", 0);

        // Set the data to the views
        tvPetitionTopic.setText(topic);
        tvPetitionDetails.setText(details);
        tvMinimumSigned.setText("Minimum Signatures: " + minSigned);
        tvDateEnds.setText("Ends On: " + dateEnds);
        tvPetitionType.setText("Type: " + type);
        tvCount.setText(currentCount + "/" +minSigned);

        // Handle the sign petition button
        btnSignPetition.setOnClickListener(v -> {
            // Handle petition signing here (e.g., save to Firestore)
            currentCount++;
            tvCount.setText(currentCount + "/" +minSigned);
            Toast.makeText(this, "Petition Signed!", Toast.LENGTH_SHORT).show();
        });
    }
}