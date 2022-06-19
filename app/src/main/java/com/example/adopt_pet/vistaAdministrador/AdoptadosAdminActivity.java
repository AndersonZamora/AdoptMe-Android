package com.example.adopt_pet.vistaAdministrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adopt_pet.Adapter.AdapterAdopciones;
import com.example.adopt_pet.Adapter.AdapterPublication;
import com.example.adopt_pet.Adapter.receiveId;
import com.example.adopt_pet.DetalleAdopcionActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.models.adopciones;
import com.example.adopt_pet.models.mascota;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdoptadosAdminActivity extends AppCompatActivity implements receiveId {

    RecyclerView mRecyclerView;
    MessageShow messageShow;
    FirebaseFirestore db;
    AdapterAdopciones mAdopciones;
    receiveId acction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adoptados_admin);
        init();
        recuperarAdopciones();
    }

    void recuperarAdopciones() {
        messageShow.showProgress();
        db.collection("Adopciones")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        messageShow.dismissProgress();
                        return;
                    }

                    List<adopciones> mascotaList = new ArrayList<>();
                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        adopciones mas = doc.toObject(adopciones.class).withId(doc.getId());
                        mascotaList.add(mas);
                    }
                    messageShow.dismissProgress();
                    mAdopciones = new AdapterAdopciones(mascotaList, acction);
                    mRecyclerView.setAdapter(mAdopciones);
                });
    }

    void init() {
        acction = this;
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.rv_adopciones);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void receivePublication(String id) {
        Intent intent = new Intent(AdoptadosAdminActivity.this, DetalleAdopcionActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }
}