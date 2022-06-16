package com.example.adopt_pet.vistaAdministrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.example.adopt_pet.R;
import com.example.adopt_pet.autenticacion.VistaRegistrarseActivity;
import com.example.adopt_pet.ayudantes.listSpinner;
import com.example.adopt_pet.ayudantes.validaciones;
import com.example.adopt_pet.models.mascota;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class DetallePublicacionesActivity extends AppCompatActivity {

    FirebaseFirestore db;
    DocumentReference docRef;
    String uid;
    TextInputLayout nombreD;
    TextInputLayout razaD;
    TextInputLayout edadD;
    TextInputLayout contactoD;
    TextInputLayout descripcionD;
    ImageView publication_image_d;
    MessageShow messageShow;
    Zoomy.Builder builder;
    Spinner spinnerPri;
    Button update;
    validaciones mValidations;
    mascota data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicaciones);

        uid = getIntent().getStringExtra("uid");
        init();
        obetnerPublicacion();

        builder.target(publication_image_d)
                .animateZooming(false)
                .enableImmersiveMode(false);
        builder.register();

        update.setOnClickListener(view -> validate());
    }

    void validate() {

        if (!mValidations.handleValidate(nombreD) | !mValidations.handleValidate(razaD) | !mValidations.handleValidate(edadD) |
                !mValidations.handleValidate(contactoD) | !mValidations.handleValidate(descripcionD)) {
            return;
        }
        updadate();
    }

    void updadate() {

        String nombre = Objects.requireNonNull(nombreD.getEditText()).getText().toString().trim();
        String raza = Objects.requireNonNull(razaD.getEditText()).getText().toString().trim();
        String edad = Objects.requireNonNull(edadD.getEditText()).getText().toString().trim();
        String contacto = Objects.requireNonNull(contactoD.getEditText()).getText().toString().trim();
        String descripcion = Objects.requireNonNull(descripcionD.getEditText()).getText().toString().trim();
        String tipo = spinnerPri.getSelectedItem().toString().trim();

        HashMap<String, Object> map = new HashMap<>();

        if (!nombre.equals(data.getNombre())) {
            map.put("nombre", nombre);
        }

        if (!raza.equals(data.getRaza())) {
            map.put("raza", raza);
        }

        if (!edad.equals(data.getEdad())) {
            map.put("edad", edad);
        }

        if (!contacto.equals(data.getContacto())) {
            map.put("contacto", contacto);
        }

        if (!descripcion.equals(data.getDescripcion())) {
            map.put("descripcion", descripcion);
        }

        if (!tipo.equals(data.getTipo())) {
            map.put("tipo", tipo);
        }

        messageShow.showProgress();
        db.collection("Publicaciones").document(uid)
                .update(map).addOnSuccessListener(unused -> {
                    Toast.makeText(DetallePublicacionesActivity.this, "publicación actualizada ", Toast.LENGTH_SHORT).show();
                    messageShow.dismissProgress();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    messageShow.dismissProgress();
                    Toast.makeText(DetallePublicacionesActivity.this, "Error al actualizar publicación", Toast.LENGTH_SHORT).show();
                });
    }

    void setData() {

        Objects.requireNonNull(nombreD.getEditText()).setText(data.getNombre());
        Objects.requireNonNull(razaD.getEditText()).setText(data.getRaza());
        Objects.requireNonNull(edadD.getEditText()).setText(data.getEdad());
        Objects.requireNonNull(contactoD.getEditText()).setText(data.getContacto());
        Objects.requireNonNull(descripcionD.getEditText()).setText(data.getDescripcion());

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

        ArrayAdapter<String> mSpinnerPri = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, listSpinner.initSpinner(data.getTipo())
        );

        spinnerPri.setAdapter(mSpinnerPri);

        messageShow.dismissProgress();
    }

    void obetnerPublicacion() {
        messageShow.showProgress();
        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                messageShow.dismissProgress();
                Toast.makeText(DetallePublicacionesActivity.this, "Error al obtener publicación", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            if (snapshot != null && snapshot.exists()) {
                data = snapshot.toObject(mascota.class);
                if (data != null) {
                    setData();
                }
            }
        });
    }

    void init() {
        nombreD = findViewById(R.id.nombreD);
        razaD = findViewById(R.id.razaD);
        edadD = findViewById(R.id.edadD);
        contactoD = findViewById(R.id.contactoD);
        descripcionD = findViewById(R.id.descripcionD);
        publication_image_d = findViewById(R.id.publication_image_d);
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        if (uid != null && !Objects.equals(uid, "")) {
            docRef = db.collection("Publicaciones").document(uid);
        }
        builder = new Zoomy.Builder(this);
        spinnerPri = findViewById(R.id.spinner_pri);
        mValidations = new validaciones(getApplicationContext());
        update = findViewById(R.id.update);
    }
}