package com.example.dahlia.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.dahlia.R;
import com.example.dahlia.adapters.AchievementAdapter;
import com.example.dahlia.databinding.ActivityAchievementsBinding;
import com.example.dahlia.databinding.DialogAchievementDetailsBinding;
import com.example.dahlia.models.Achievement;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AchievementsActivity extends BaseActivity implements AchievementAdapter.OnItemClickListener {

    private ActivityAchievementsBinding binding;

    private PreferenceManager preferenceManager;
    private List<Achievement> achievements;
    private AchievementAdapter achievementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAchievementsBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        // Initialize PreferenceManager
        preferenceManager = new PreferenceManager(getApplicationContext());

        // Initialize achievements list and adapter
        achievements = new ArrayList<>();
        achievementAdapter = new AchievementAdapter(achievements, this); // Pass 'this' as the listener

        // Set up RecyclerView
        binding.achievement.setLayoutManager(new GridLayoutManager(this, 2));
        binding.achievement.setAdapter(achievementAdapter);

        // Fetch achievements from Firestore
        fetchAchievementsFromFirestore();

        // Set profile image and name
        binding.profileImage.setImageBitmap(decodeImage(preferenceManager.getString(Constants.KEY_IMAGE)));
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void fetchAchievementsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Constants.KEY_COLLECTION_ACHIEVEMENT)
                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID)) // Filter by userId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map the document to your Achievement model
                            String title = document.getString("title");
                            String image = document.getString("image");
                            String description = document.getString("description");
                            Achievement achievement = new Achievement(image, title, description);

                            // Add the achievement to your list
                            achievements.add(achievement);
                        }
                        // Notify the adapter that the data has changed
                        achievementAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error fetching documents", task.getException());
                        Toast.makeText(this, "Error fetching documents: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemClick(Achievement achievement) {
        // Show the popup when an item is clicked
        showAchievementDetailsDialog(achievement);
    }

    private void showAchievementDetailsDialog(Achievement achievement) {
        // Create a new Dialog
        Dialog dialog = new Dialog(this);

        // Inflate the dialog layout using a new binding instance
        DialogAchievementDetailsBinding dialogBinding = DialogAchievementDetailsBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        // Set data to views

        dialogBinding.textViewTitle.setText(achievement.getTitle());
        dialogBinding.textViewDescription.setText(achievement.getDescription());
        dialogBinding.imageViewAchievement.setImageBitmap(decodeImage(achievement.getImage()));

        // Set click listener for the close button
        dialogBinding.close.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}