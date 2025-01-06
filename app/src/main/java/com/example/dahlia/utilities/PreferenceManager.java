package com.example.dahlia.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dahlia.models.EmergencyContact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {
    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void putEmergencyContacts(List<EmergencyContact> contacts) {
        Gson gson = new Gson();
        String json = gson.toJson(contacts);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_EMERGENCY_CONTACT, json);
        editor.apply();
    }

    public List<EmergencyContact> getEmergencyContacts() {
        String json = sharedPreferences.getString(Constants.KEY_EMERGENCY_CONTACT, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<EmergencyContact>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
