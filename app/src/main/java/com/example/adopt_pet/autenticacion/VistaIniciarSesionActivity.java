package com.example.adopt_pet.autenticacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adopt_pet.MainActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.validaciones;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class VistaIniciarSesionActivity extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;
    Button login;
    validaciones mValidations;
    TextView regis;
    TextView recup;
    MessageShow messageShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_iniciar_sesion);
        init();

        login.setOnClickListener(view -> {

            if (!mValidations.handleEmailValidation(email) | !mValidations.handlePasswordValidation(password)) {
                return;
            }

            String emailS = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
            String passwordS = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
            singInWithEmailAndPassword(emailS, passwordS);
        });

        regis.setOnClickListener(view -> {
            startActivity(new Intent(VistaIniciarSesionActivity.this, VistaRegistrarseActivity.class));
        });
        recup.setOnClickListener(view -> {
            startActivity(new Intent(VistaIniciarSesionActivity.this, RestablecimientoActivity.class));
        });
    }

    void init() {

        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        regis = findViewById(R.id.regis);
        recup = findViewById(R.id.recup);
        mValidations = new validaciones(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
    }

    public void singInWithEmailAndPassword(String email, String password) {
        messageShow.showProgress();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                messageShow.dismissProgress();
                Intent intent = new Intent(VistaIniciarSesionActivity.this, validarRolActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(e -> {
            messageShow.dismissProgress();
            messageShow.showMessageV(e.getMessage());
        });
    }
}