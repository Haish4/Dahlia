package com.example.dahlia.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dahlia.R;
import com.example.dahlia.databinding.ActivitySosBinding;
import com.example.dahlia.utilities.Constants;
import com.example.dahlia.utilities.PreferenceManager;

public class SosActivity extends AppCompatActivity {

    ActivitySosBinding binding;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySosBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setBinding();

    }

    private void setBinding(){
        binding.userName.setText("STAY SAFE " + preferenceManager.getString(Constants.KEY_NAME));
    }

}