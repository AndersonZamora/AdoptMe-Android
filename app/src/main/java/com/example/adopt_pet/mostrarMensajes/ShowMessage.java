package com.example.adopt_pet.mostrarMensajes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.constants;

public class ShowMessage extends DialogFragment {

    String typeMessage;
    String descriptionMessage;

    public ShowMessage() {
        //Required constructor
    }

    @NonNull
    public static ShowMessage newInstance(String typeMessage, String descriptionMessage) {

        ShowMessage showMessage = new ShowMessage();
        Bundle bundle = new Bundle();
        bundle.putString(constants.TYPE_MESSAGE, typeMessage);
        bundle.putString(constants.DESCRIPTION_MESSAGE, descriptionMessage);
        showMessage.setArguments(bundle);

        return showMessage;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            typeMessage = getArguments().getString(constants.TYPE_MESSAGE);
            descriptionMessage = getArguments().getString(constants.DESCRIPTION_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_message, container);

        TextView messageType = view.findViewById(R.id.type_message);
        TextView messageDescription = view.findViewById(R.id.description_message);
        Button messageConfirm = view.findViewById(R.id.confirm_message);

        messageType.setText(typeMessage);
        messageDescription.setText(descriptionMessage);

        messageConfirm.setOnClickListener(view1 -> dismiss());

        return view;
    }
}
