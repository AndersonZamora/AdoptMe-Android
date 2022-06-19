package com.example.adopt_pet.vistaAdministrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.adopt_pet.MainActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.autenticacion.validarRolActivity;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.usuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PerfilAdminActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);

        init();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                progress_circular_person.setVisibility(View.GONE);
            }

            if (snapshot != null && snapshot.exists()) {
                usuario getUser = snapshot.toObject(usuario.class);
                assert getUser != null;
                setData(getUser);
            }
        });

        signOff.setOnClickListener(view -> {
            manager.clearPreference();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(PerfilAdminActivity.this, MainActivity.class);
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

    void init() {
        manager = new preferenceManager(PerfilAdminActivity.this);
        progress_circular_person = findViewById(R.id.progress_circular_person);
        nombreP = findViewById(R.id.nombreP);
        apellidosp = findViewById(R.id.apellidosp);
        emailp = findViewById(R.id.emailp);
        celularP = findViewById(R.id.celularP);
        direccionP = findViewById(R.id.direccionP);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        docRef = db.collection("Usuarios").document(uid);
        signOff = findViewById(R.id.signOff);
    }
}