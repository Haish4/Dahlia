package com.example.dahlia.utilities;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Default empty implementation
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Default empty implementation
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Default empty implementation
    }
}
