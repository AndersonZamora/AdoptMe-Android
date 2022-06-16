package com.example.adopt_pet.mostrarMensajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.adopt_pet.R;

public class showProgressBar extends DialogFragment {

    public showProgressBar() {
        //Required constructor
    }

    @NonNull
    public static showProgressBar newInstance() {
        return new showProgressBar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.show_progress, container);
    }
}
