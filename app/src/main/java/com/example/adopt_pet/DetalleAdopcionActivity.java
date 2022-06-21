package com.example.adopt_pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adopt_pet.models.adopciones;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetalleAdopcionActivity extends AppCompatActivity {

    TextView nameS;
    TextView estadoS;
    TextView fechaS;
    TextView nuemroU;
    ImageView whatsapp;
    ImageView publication_image_s;
    ImageView llamada;
    MessageShow messageShow;
    FirebaseFirestore db;
    DocumentReference docRef;
    String uid;
    adopciones adop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_adopcion);

        uid = getIntent().getStringExtra("uid");

        init();
        getAdopcion();

        whatsapp.setOnClickListener(view -> {
            String url = "https://api.whatsapp.com/send?phone=" + "+51" + adop.getNumero();
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(DetalleAdopcionActivity.this, "La aplicación Whatsapp no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });
        llamada.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + adop.getNumero()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(DetalleAdopcionActivity.this, "La aplicación teléfono no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAdopcion() {

        messageShow.showProgress();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                messageShow.dismissProgress();
            }

            if (snapshot != null && snapshot.exists()) {
                adop = snapshot.toObject(adopciones.class);
                assert adop != null;
                setData();
                messageShow.dismissProgress();
            }
        });
    }

    void setData() {

        if (adop != null) {
            nameS.setText(adop.getNombre());
            estadoS.setText(adop.getInfo());
            fechaS.setText(adop.getFecha());
            nuemroU.setText(adop.getNumero());

            if (adop.getFoto() != null && !Objects.equals(adop.getFoto(), "")) {
                Picasso.get()
                        .load(adop.getFoto())
                        .placeholder(R.color.black)
                        .error(R.color.purple_200)
                        .into(publication_image_s, new Callback() {
                            @Override
                            public void onSuccess() {
                                publication_image_s.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                publication_image_s.setVisibility(View.VISIBLE);
                                publication_image_s.setImageResource(R.drawable.portada);
                            }
                        });
            }
        }
    }

    void init() {
        estadoS = findViewById(R.id.estadoS);
        fechaS = findViewById(R.id.fechaS);
        nameS = findViewById(R.id.nameS);
        nuemroU = findViewById(R.id.nuemroU);
        whatsapp = findViewById(R.id.whatsapp);
        llamada = findViewById(R.id.llamada);
        publication_image_s = findViewById(R.id.publication_image_s);
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();

        try {
            if (uid != null && !uid.equals("")) {
                docRef = db.collection("Adopciones").document(uid);
            }
        } catch (Exception e) {
            onBackPressed();
        }
    }
}