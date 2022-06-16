package com.example.adopt_pet.vistaUsuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.adopt_pet.MainActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.usuario;
import com.example.adopt_pet.vistaAdministrador.PerfilAdminActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FragmentCuenta extends Fragment {

    TextInputLayout nombreP;
    TextInputLayout apellidosp;
    TextInputLayout emailp;
    TextInputLayout celularP;
    TextInputLayout direccionP;
    FirebaseFirestore db;
    DocumentReference docRef;
    FirebaseUser user;
    String uid;
    ProgressBar progress_circular_person;
    Button signOff;
    preferenceManager manager;

    public FragmentCuenta() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cuenta, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        docRef.addSnapshotListener((snapshot, e) -> {
            if (snapshot != null && snapshot.exists()) {
                usuario getUser = snapshot.toObject(usuario.class);
                assert getUser != null;
                setData(getUser);
            }
        });

        signOff.setOnClickListener(view2 -> {
            manager.clearPreference();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    void setData(@NonNull usuario getUser) {
        Objects.requireNonNull(nombreP.getEditText()).setText(getUser.getNombres());
        Objects.requireNonNull(apellidosp.getEditText()).setText(getUser.getApellidos());
        Objects.requireNonNull(celularP.getEditText()).setText(getUser.getCelular());
        Objects.requireNonNull(direccionP.getEditText()).setText(getUser.getDireccion());
        Objects.requireNonNull(emailp.getEditText()).setText(getUser.getCorreo());
        progress_circular_person.setVisibility(View.GONE);
    }

    private void init(@NonNull View view) {
        manager = new preferenceManager(requireActivity());
        progress_circular_person = view.findViewById(R.id.progress_circular_person);
        nombreP = view.findViewById(R.id.nombreP);
        apellidosp = view.findViewById(R.id.apellidosp);
        emailp = view.findViewById(R.id.emailp);
        celularP = view.findViewById(R.id.celularP);
        direccionP = view.findViewById(R.id.direccionP);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        docRef = db.collection("Usuarios").document(uid);
        signOff = view.findViewById(R.id.signOff);
    }
}