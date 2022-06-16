package com.example.adopt_pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.adopt_pet.autenticacion.VistaIniciarSesionActivity;
import com.example.adopt_pet.autenticacion.validarRolActivity;
import com.example.adopt_pet.ayudantes.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        ImageView logo = findViewById(R.id.app_logo);
        logo.setAnimation(topAnim);
        new Handler().postDelayed(this::checkState, constants.SPLASH_SCREEN);
    }
    public void checkState() {
        Intent intent;

        if (mUser != null) {
            intent = new Intent(MainActivity.this, validarRolActivity.class);
        } else {
            intent = new Intent(MainActivity.this, VistaIniciarSesionActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}