package com.example.adopt_pet.autenticacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.validaciones;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VistaRegistrarseActivity extends AppCompatActivity {

    TextInputLayout nombreF;
    TextInputLayout apellidosF;
    TextInputLayout direccionF;
    TextInputLayout celularF;
    TextInputLayout correoF;
    TextInputLayout contrasenia1F;
    TextInputLayout contrasenia2F;
    Button registro;
    RadioButton rbAccept;
    validaciones mValidations;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    MessageShow messageShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_registrarse);
        init();

        registro.setOnClickListener(view -> {
            if (!mValidations.handleValidate(nombreF) | !mValidations.handleValidate(apellidosF) | !mValidations.handleValidate(direccionF) |
                    !mValidations.handleValidate(celularF) | !mValidations.handleEmailValidation(correoF) | !mValidations.handleValidate(contrasenia1F)
                    | !mValidations.handleValidate(contrasenia2F)) {
                return;
            }

            String contrasenia1 = Objects.requireNonNull(contrasenia1F.getEditText()).getText().toString().trim();
            String contrasenia2 = Objects.requireNonNull(contrasenia2F.getEditText()).getText().toString().trim();

            if (!contrasenia1.equals(contrasenia2)) {
                Toast.makeText(VistaRegistrarseActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            if (contrasenia1.length() < 6) {
                Toast.makeText(VistaRegistrarseActivity.this, "Las contraseña tiene que tener un minimo de 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            createUserWithEmailAndPassword();

        });
        rbAccept.setOnClickListener(view -> registro.setEnabled(rbAccept.isChecked()));
    }

    void init() {

        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mValidations = new validaciones(this);
        nombreF = findViewById(R.id.nombreF);
        apellidosF = findViewById(R.id.apellidosF);
        direccionF = findViewById(R.id.direccionF);
        celularF = findViewById(R.id.celularF);
        correoF = findViewById(R.id.correoF);
        contrasenia1F = findViewById(R.id.contrasenia1F);
        contrasenia2F = findViewById(R.id.contrasenia2F);
        registro = findViewById(R.id.registrar);
        rbAccept = findViewById(R.id.rbAccept);
    }

    void createUserWithEmailAndPassword() {

        messageShow.showProgress();
        String nombre = Objects.requireNonNull(nombreF.getEditText()).getText().toString().trim();
        String apellidos = Objects.requireNonNull(apellidosF.getEditText()).getText().toString().trim();
        String direccion = Objects.requireNonNull(direccionF.getEditText()).getText().toString().trim();
        String celular = Objects.requireNonNull(celularF.getEditText()).getText().toString().trim();
        String correo = Objects.requireNonNull(correoF.getEditText()).getText().toString().trim();
        String contrasenia = Objects.requireNonNull(contrasenia1F.getEditText()).getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(correo, contrasenia)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        Map<String, Object> map = new HashMap<>();
                        map.put("nombres", nombre);
                        map.put("apellidos", apellidos);
                        map.put("direccion", direccion);
                        map.put("celular", celular);
                        map.put("correo", correo);
                        map.put("privilegio", "Usuario");
                        createNewUser(map, user.getUid());
                    } else {
                        Toast.makeText(VistaRegistrarseActivity.this, "El registro falla, intenten de nuevo si el problema persiste contactar con nosotros",
                                Toast.LENGTH_SHORT).show();
                        messageShow.dismissProgress();
                    }
                });
    }

    void createNewUser(Map<String, Object> map, String uid) {

        db.collection("Usuarios").document(uid)
                .set(map)
                .addOnSuccessListener(aVoid -> {
                    messageShow.dismissProgress();
                    Intent intent = new Intent(VistaRegistrarseActivity.this, validarRolActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    messageShow.showMessageV(e.getMessage());
                    messageShow.dismissProgress();
                });
    }
}