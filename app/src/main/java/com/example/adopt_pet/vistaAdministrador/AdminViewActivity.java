package com.example.adopt_pet.vistaAdministrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.adopt_pet.R;

public class AdminViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        ImageView publications_img = findViewById(R.id.publications_img);
        ImageView sign_off_img = findViewById(R.id.sign_off_img);
        ImageView requests_img = findViewById(R.id.requests_img);
        ImageView pharmacist_img = findViewById(R.id.pharmacist_img);
        publications_img.setOnClickListener(view -> startActivity(new Intent(AdminViewActivity.this, PublicacionesAdminActivity.class)));
        sign_off_img.setOnClickListener(view -> startActivity(new Intent(AdminViewActivity.this, PerfilAdminActivity.class)));
        requests_img.setOnClickListener(view -> startActivity(new Intent(AdminViewActivity.this, SolicitudesAdminActivity.class)));
        pharmacist_img.setOnClickListener(view -> startActivity(new Intent(AdminViewActivity.this, AdoptadosAdminActivity.class)));
    }
}