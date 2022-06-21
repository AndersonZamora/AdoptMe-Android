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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class DetalleSolicitudActivity extends AppCompatActivity {

    TextView nameS;
    TextView mensajeS;
    TextView razaS;
    TextView tipoS;
    TextView nameU;
    TextView nuemroU;
    ImageView whatsapp;
    ImageView publication_image_s;
    ImageView llamada;
    Button rechazar;
    Button aprobar;
    Button adopcion;
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
            String url = "https://api.whatsapp.com/send?phone=" + "+51" + sol.getNumeroUsuario() + "&text=" + sol.getNombreUsuario();
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
        rechazar.setOnClickListener(view -> updateSolicitud("Rechazada"));
        aprobar.setOnClickListener(view -> updateSolicitud("Aprobada"));
        adopcion.setOnClickListener(view -> adoptar());
    }

    void adoptar() {

        messageShow.showProgress();
        HashMap<String, Object> map = new HashMap<>();
        map.put("nombre", sol.getNombreMascota());
        map.put("foto", sol.getFoto());
        map.put("info", "Encontró un hogar");
        map.put("fecha", DateFormat.getDateInstance().format(new Date()));
        map.put("solicitante", sol.getNombreUsuario());
        map.put("numero", sol.getNumeroUsuario());

        db.collection("Adopciones")
                .add(map)
                .addOnSuccessListener(aVoid -> {
                    db.collection("Publicaciones").document(sol.getMascotaID())
                            .delete()
                            .addOnSuccessListener(aVoiddd -> {
                                db.collection("Solicitudes").document(sol.getUsuarioID())
                                        .delete()
                                        .addOnSuccessListener(aVoidd -> {
                                            messageShow.dismissProgress();
                                            Toast.makeText(DetalleSolicitudActivity.this, "Adoptado", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        });
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DetalleSolicitudActivity.this, "No Adoptado", Toast.LENGTH_SHORT).show();
                    messageShow.showMessageV(e.getMessage());
                    messageShow.dismissProgress();
                });
    }

    void updateSolicitud(String tipo) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("estado", tipo);
        messageShow.showProgress();
        db.collection("Solicitudes").document(uid)
                .update(map).addOnSuccessListener(unused -> {
                    if (tipo.equals("Aprobada")) {
                        HashMap<String, Object> mapP = new HashMap<>();
                        mapP.put("adopcion", "si");
                        db.collection("Publicaciones").document(sol.getMascotaID()).update(mapP).addOnSuccessListener(unused1 -> {
                            messageShow.dismissProgress();
                            Toast.makeText(DetalleSolicitudActivity.this, "Solicitud " + tipo, Toast.LENGTH_SHORT).show();
                            messageShow.dismissProgress();
                            onBackPressed();
                        });
                    } else {
                        messageShow.dismissProgress();
                        Toast.makeText(DetalleSolicitudActivity.this, "Solicitud " + tipo, Toast.LENGTH_SHORT).show();
                        messageShow.dismissProgress();
                        onBackPressed();
                    }
                }).addOnFailureListener(e -> {
                    messageShow.dismissProgress();
                    Toast.makeText(DetalleSolicitudActivity.this, "Error al " + tipo + " solicitud ", Toast.LENGTH_SHORT).show();
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

            if (sol.getEstado().equals("Rechazada")) {
                aprobar.setVisibility(View.VISIBLE);
                rechazar.setVisibility(View.GONE);
                mensajeS.setVisibility(View.GONE);
                adopcion.setVisibility(View.GONE);
            } else if (sol.getEstado().equals("Aprobada")) {
                rechazar.setVisibility(View.GONE);
                aprobar.setVisibility(View.GONE);
                mensajeS.setVisibility(View.VISIBLE);
                adopcion.setVisibility(View.VISIBLE);
            }
        }
    }

    void init() {
        nameS = findViewById(R.id.nameS);
        razaS = findViewById(R.id.razaS);
        tipoS = findViewById(R.id.tipoS);
        mensajeS = findViewById(R.id.mensajeS);
        nameU = findViewById(R.id.nameU);
        nuemroU = findViewById(R.id.nuemroU);
        whatsapp = findViewById(R.id.whatsapp);
        llamada = findViewById(R.id.llamada);
        rechazar = findViewById(R.id.rechazar);
        aprobar = findViewById(R.id.aprobar);
        adopcion = findViewById(R.id.adopcion);
        publication_image_s = findViewById(R.id.publication_image_s);

        manager = new preferenceManager(DetalleSolicitudActivity.this);
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();

        try {
            if (uid != null && !uid.equals("")) {
                docRef = db.collection("Solicitudes").document(uid);
            }
        } catch (Exception e) {
            onBackPressed();
        }
    }
}