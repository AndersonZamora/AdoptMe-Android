package com.example.adopt_pet.vistaAdministrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.constants;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.solicitud;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.example.adopt_pet.vistaUsuario.mascotaDetalleActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetalleSolicitudActivity extends AppCompatActivity {

    TextView verFicha;
    TextView nameS;
    TextView razaS;
    TextView tipoS;
    TextView nameU;
    TextView nuemroU;
    ImageView whatsapp;
    ImageView publication_image_s;
    ImageView llamada;
    Button rechazar;
    Button aprobar;

    FirebaseFirestore db;
    DocumentReference docRef;
    String uid;
    MessageShow messageShow;
    preferenceManager manager;
    solicitud sol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud);

        uid = getIntent().getStringExtra("uid");

        init();
        getSolicitud();

        whatsapp.setOnClickListener(view -> {
            String url = "https://api.whatsapp.com/send?phone=" + sol.getNumeroUsuario() + "&text=" + sol.getNombreUsuario();
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(DetalleSolicitudActivity.this, "La aplicación Whatsapp no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });
        llamada.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + sol.getNumeroUsuario()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(DetalleSolicitudActivity.this, "La aplicación teléfono no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSolicitud() {

        messageShow.showProgress();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                messageShow.dismissProgress();
            }

            if (snapshot != null && snapshot.exists()) {
                sol = snapshot.toObject(solicitud.class);
                assert sol != null;
                manager.putString(constants.SOLOCITUD_USUARIO, sol.getEstado());
                setData();
                messageShow.dismissProgress();
            }
        });
    }

    void setData() {

        if (sol != null) {
            nameS.setText(sol.getNombreMascota());
            razaS.setText(sol.getRazaMascota());
            tipoS.setText(sol.getTipoMascota());

            nameU.setText(sol.getNombreUsuario());
            nuemroU.setText(sol.getNumeroUsuario());

            if (sol.getFoto() != null && !Objects.equals(sol.getFoto(), "")) {
                Picasso.get()
                        .load(sol.getFoto())
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
        verFicha = findViewById(R.id.verFicha);
        nameS = findViewById(R.id.nameS);
        razaS = findViewById(R.id.razaS);
        tipoS = findViewById(R.id.tipoS);
        nameU = findViewById(R.id.nameU);
        nuemroU = findViewById(R.id.nuemroU);
        whatsapp = findViewById(R.id.whatsapp);
        llamada = findViewById(R.id.llamada);
        rechazar = findViewById(R.id.rechazar);
        aprobar = findViewById(R.id.aprobar);
        publication_image_s = findViewById(R.id.publication_image_s);

        manager = new preferenceManager(DetalleSolicitudActivity.this);
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();

        if (uid != null && !uid.equals("")) {
            docRef = db.collection("Solicitudes").document(uid);
        }
    }
}