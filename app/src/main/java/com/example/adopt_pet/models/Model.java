package com.example.adopt_pet.models;

import android.support.annotation.NonNull;

public class Model {

    public String conId;

    public <T extends Model> T withId(@NonNull final String conId) {
        this.conId = conId;
        //noinspection unchecked
        return (T) this;
    }
}
