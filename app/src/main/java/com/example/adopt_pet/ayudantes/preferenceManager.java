package com.example.adopt_pet.ayudantes;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class preferenceManager implements preferenceManagerI {

    private final SharedPreferences sharedPreferences;

    public preferenceManager(@NonNull Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    @Override
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void clearPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
