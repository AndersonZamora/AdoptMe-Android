package com.example.adopt_pet.ayudantes;

public interface preferenceManagerI {

    void putBoolean(String key, Boolean value);

    boolean getBoolean(String key);

    void putString(String key, String value);

    String getString(String key);

    void clearPreference();
}
