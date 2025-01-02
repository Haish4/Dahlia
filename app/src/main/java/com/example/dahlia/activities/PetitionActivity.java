package com.example.dahlia.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import java.util.Calendar;

public class PetitionActivity extends AppCompatActivity {

    ImageView IVUploadPhoto;

    ActivityResultLauncher<Intent> resultLauncher;

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

        TextView TVDatePetitionEnds = findViewById(R.id.TVDatePetitionEnds);

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