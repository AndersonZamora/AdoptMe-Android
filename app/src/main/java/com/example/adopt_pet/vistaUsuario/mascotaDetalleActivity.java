package com.example.adopt_pet.vistaUsuario;

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

import com.ablanco.zoomy.Zoomy;
import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.constants;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.ayudantes.validaciones;
import com.example.adopt_pet.models.mascota;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class mascotaDetalleActivity extends AppCompatActivity {

    FirebaseFirestore db;
    DocumentReference docRef;
    String uid;
    ImageView publication_image_d;
    MessageShow messageShow;
    Zoomy.Builder builder;
    validaciones mValidations;
    mascota data;
    TextView estado;
    TextView descripcionT;
    TextView nameT;
    TextView razaT;
    TextView tipoT;
    TextView edadT;
    TextView fechaT;
    TextView mensajeEstado;
    TextView mensajeEstadoPen;
    ImageView whatsapp;
    ImageView llamada;
    Button sendSolicitud;
    preferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle);

        uid = getIntent().getStringExtra("uid");
        init();
        obetnerMascota();

        builder.target(publication_image_d)
                .animateZooming(false)
                .enableImmersiveMode(false);
        builder.register();

        whatsapp.setOnClickListener(view -> {
            String url = "https://api.whatsapp.com/send?phone=" + "+51" + data.getContacto();
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(mascotaDetalleActivity.this, "La aplicación Whatsapp no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });
        llamada.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + data.getContacto()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(mascotaDetalleActivity.this, "La aplicación teléfono no está instalada en su celular", Toast.LENGTH_SHORT).show();
            }
        });
        sendSolicitud.setOnClickListener(view -> enviarSolicitud());
    }

    private void enviarSolicitud() {

        messageShow.showProgress();
        String nombreUsuario = manager.getString(constants.NOMBRE_USUARIO);
        String numeroUsuario = manager.getString(constants.NUMERO_USUARIO);
        String usuarioID = manager.getString(constants.UID);

        Map<String, Object> map = new HashMap<>();
        map.put("nombreUsuario", nombreUsuario);
        map.put("numeroUsuario", numeroUsuario);
        map.put("usuarioID", usuarioID);
        map.put("nombreMascota", data.getNombre());
        map.put("razaMascota", data.getRaza());
        map.put("tipoMascota", data.getTipo());
        map.put("mascotaID", uid);
        map.put("estado", "Pendiente");
        map.put("foto", data.getFoto());
        map.put("info", data.getEstado());
        map.put("fecha", DateFormat.getDateInstance().format(new Date()));

        db.collection("Solicitudes").document(usuarioID)
                .set(map)
                .addOnSuccessListener(aVoid -> {
                    messageShow.dismissProgress();
                    Toast.makeText(mascotaDetalleActivity.this, "Solicitud Enviada", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(mascotaDetalleActivity.this, "Solicitud no Enviada", Toast.LENGTH_SHORT).show();
                    messageShow.showMessageV(e.getMessage());
                    messageShow.dismissProgress();
                });
    }

    void obetnerMascota() {
        messageShow.showProgress();
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                messageShow.dismissProgress();
            }
            if (snapshot != null && snapshot.exists()) {
                data = snapshot.toObject(mascota.class);
                if (data != null) {
                    setData();
                }
            }
        });
    }

    void setData() {
        if (data.getFoto() != null && !Objects.equals(data.getFoto(), "")) {
            Picasso.get()
                    .load(data.getFoto())
                    .placeholder(R.color.black)
                    .error(R.color.purple_200)
                    .into(publication_image_d, new Callback() {
                        @Override
                        public void onSuccess() {
                            publication_image_d.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            publication_image_d.setVisibility(View.VISIBLE);
                            publication_image_d.setImageResource(R.drawable.portada);
                        }
                    });
        }
        estado.setText(data.getEstado());
        descripcionT.setText(data.getDescripcion());
        nameT.setText(data.getNombre());
        razaT.setText(data.getRaza());
        tipoT.setText(data.getTipo());
        edadT.setText(data.getEdad());
        fechaT.setText(data.getFechaPublication());

        String soli = manager.getString(constants.SOLOCITUD_USUARIO);

        if (data.getAdopcion().equals("Aprodado")) {
            sendSolicitud.setVisibility(View.GONE);
            mensajeEstado.setVisibility(View.VISIBLE);
        } else if (soli != null && !soli.equals("")) {
            sendSolicitud.setVisibility(View.GONE);
            mensajeEstadoPen.setVisibility(View.VISIBLE);
        }

        messageShow.dismissProgress();
    }

    void init() {
        manager = new preferenceManager(mascotaDetalleActivity.this);
        publication_image_d = findViewById(R.id.image_mascota);
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        if (uid != null && !Objects.equals(uid, "")) {
            docRef = db.collection("Publicaciones").document(uid);
        }
        builder = new Zoomy.Builder(this);
        mValidations = new validaciones(getApplicationContext());
        estado = findViewById(R.id.estadoT);
        descripcionT = findViewById(R.id.descripcionT);
        nameT = findViewById(R.id.nameT);
        razaT = findViewById(R.id.razaT);
        tipoT = findViewById(R.id.tipoT);
        edadT = findViewById(R.id.edadT);
        fechaT = findViewById(R.id.fechaT);
        whatsapp = findViewById(R.id.whatsapp);
        llamada = findViewById(R.id.llamada);
        sendSolicitud = findViewById(R.id.sendSolicitud);
        mensajeEstado = findViewById(R.id.mensajeEstado);
        mensajeEstadoPen = findViewById(R.id.mensajeEstadoPen);
    }
}