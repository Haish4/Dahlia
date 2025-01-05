package com.example.dahlia.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dahlia.databinding.ActivityWeeklychallengesBinding;

public class WeeklyChallengesActivity extends BaseActivity {

    private ActivityWeeklychallengesBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeeklychallengesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
    }

}
