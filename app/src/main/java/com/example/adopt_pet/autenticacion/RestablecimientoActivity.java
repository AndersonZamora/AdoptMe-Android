package com.example.adopt_pet.autenticacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.validaciones;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RestablecimientoActivity extends AppCompatActivity {

    TextInputLayout email;
    Button recuperar;
    TextView mensaje;
    ProgressBar progress_recuperar;
    validaciones mValidations;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecimiento);
        init();

        recuperar.setOnClickListener(view -> {
            if (!mValidations.handleEmailValidation(email)) {
                return;
            }

            email.setVisibility(View.GONE);
            progress_recuperar.setVisibility(View.VISIBLE);

            String emailAddress = Objects.requireNonNull(email.getEditText()).getText().toString().trim();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            recuperar.setVisibility(View.GONE);
                            mensaje.setVisibility(View.VISIBLE);
                            progress_recuperar.setVisibility(View.GONE);
                        } else {
                            onBackPressed();
                        }
                    });
        });
    }

    void init() {

        auth = FirebaseAuth.getInstance();
        mValidations = new validaciones(RestablecimientoActivity.this);
        email = findViewById(R.id.respEmail);
        recuperar = findViewById(R.id.recuperar);
        mensaje = findViewById(R.id.mensaje);
        progress_recuperar = findViewById(R.id.progress_recuperar);
    }
}