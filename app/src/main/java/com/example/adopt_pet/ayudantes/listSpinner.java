package com.example.adopt_pet.ayudantes;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class listSpinner {

    @NonNull
    public static List<String> initSpinner(@NonNull String value) {

        List<String> list = new ArrayList<>();

        switch (value) {
            case "Gato":
                list.add(value);
                list.add("Perro");
                break;

            case "Perro":
                list.add(value);
                list.add("Gato");
                break;

            default:
                list.add("Gato");
                list.add("Perro");
        }
        return list;
    }
}
