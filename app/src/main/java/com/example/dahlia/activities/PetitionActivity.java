package com.example.dahlia.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahlia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PetitionActivity extends AppCompatActivity {

    ImageView IVUploadPhoto;

    ActivityResultLauncher<Intent> resultLauncher;
    private FirebaseFirestore db;

    String typedummy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_petition);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //backButton
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetitionActivity.this, MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        TextView TVDatePetitionEnds = findViewById(R.id.PetitionEndDate);

        TVDatePetitionEnds.setOnClickListener(v -> {
            //Get the current date
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //show datepicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PetitionActivity.this,
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        // Update the TextView with the selected date
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        TVDatePetitionEnds.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


        //SETUP SPINNER FOR DROPDOWN
        Spinner spinnerType = findViewById(R.id.spinnerType);

        //Create ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.type_choices,
                android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply adapter to the spinner
        spinnerType.setAdapter(adapter);
        //Set an item selected listener
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Handle item selection
                String selectedType = parent.getItemAtPosition(position).toString();
                typedummy = selectedType;
                Toast.makeText(PetitionActivity.this, "Selected Type: " + selectedType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Handle nothing selected
            }
        });

        //upload photos
        IVUploadPhoto = findViewById(R.id.IVUploadPhoto);
        registerResult();

        IVUploadPhoto.setOnClickListener(view -> pickImage());

        EditText PetitionTopic = findViewById(R.id.PetitionTopic);
        EditText PetitionDetails = findViewById(R.id.PetitionDetails);
        EditText PetitionMinSigned = findViewById(R.id.PetitionMinSigned);
        TextView PetitionEndDate = findViewById(R.id.PetitionEndDate);
        spinnerType = findViewById(R.id.spinnerType);
        Button PetitionShareButton = findViewById(R.id.PetitionShareButton);

        PetitionShareButton.setOnClickListener(v -> {
            String topic = PetitionTopic.getText().toString();
            String details = PetitionDetails.getText().toString();
            int minSigned = Integer.parseInt(PetitionMinSigned.getText().toString());
            String endDate = PetitionEndDate.getText().toString();
            String type = typedummy;

            db = FirebaseFirestore.getInstance();

            Map<String, Object> petitionData = new HashMap<>();
            petitionData.put("topic", topic);
            petitionData.put("details", details);
            petitionData.put("minSigned", minSigned);
            petitionData.put("endDate", endDate);
            petitionData.put("type", type);
            petitionData.put("count",0);

            //store in firestore
            db.collection("petition")
                    .add(petitionData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(PetitionActivity.this, "Petition added successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error adding petition", e);
                        Toast.makeText(PetitionActivity.this, "Failed to add petition", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    private void registerResult() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        try{
                            Uri imageUri = o.getData().getData();
                            IVUploadPhoto.setImageURI(imageUri);
                        }catch (Exception e){
                            Toast.makeText(PetitionActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
    }
}