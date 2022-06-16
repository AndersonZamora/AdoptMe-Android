package com.example.adopt_pet.autenticacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adopt_pet.DeactivatedUserActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.constants;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.usuario;
import com.example.adopt_pet.vistaAdministrador.AdminViewActivity;
import com.example.adopt_pet.vistaUsuario.UsersScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class validarRolActivity extends AppCompatActivity {

    FirebaseUser user;
    String uid;
    FirebaseFirestore db;
    DocumentReference docRef;
    ProgressBar progress_circular;
    TextView mensaje;
    preferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_rol);
        init();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                progress_circular.setVisibility(View.GONE);
                mensaje.setVisibility(View.VISIBLE);
            }

            if (snapshot != null && snapshot.exists()) {
                usuario user = snapshot.toObject(usuario.class);
                assert user != null;
                manager.putString(constants.NOMBRE_USUARIO, user.getNombres());
                manager.putString(constants.NUMERO_USUARIO, user.getCelular());
                manager.putString(constants.UID, uid);
                checkRole(user.getPrivilegio());
            }
        });
    }

    void init() {
        manager = new preferenceManager(validarRolActivity.this);
        progress_circular = findViewById(R.id.progress_circular);
        mensaje = findViewById(R.id.mensaje);
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("Usuarios").document(uid);
    }

    void checkRole(@NonNull String role) {
        switch (role) {
            case constants.ROL_USUARIO: {
                Intent intent = new Intent(validarRolActivity.this, UsersScreenActivity.class);
                interActivity(intent);
                break;
            }

            case constants.ROL_ADMIN: {
                Intent intent = new Intent(validarRolActivity.this, AdminViewActivity.class);
                interActivity(intent);
                break;
            }

            default:
                Intent intent = new Intent(validarRolActivity.this, DeactivatedUserActivity.class);
                interActivity(intent);
                break;
        }
    }

    private void interActivity(@NonNull Intent intent) {
        if (uid != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}