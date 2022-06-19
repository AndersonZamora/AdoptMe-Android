package com.example.adopt_pet.vistaAdministrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.adopt_pet.Adapter.AdapterSolicitud;
import com.example.adopt_pet.Adapter.receiveId;
import com.example.adopt_pet.R;
import com.example.adopt_pet.models.solicitud;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesAdminActivity extends AppCompatActivity implements receiveId {

    RecyclerView mRecyclerView;
    MessageShow messageShow;
    FirebaseFirestore db;
    receiveId acction;
    AdapterSolicitud mSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_admin);

        init();
        recuperarSolicitudes();
    }

    private void recuperarSolicitudes() {
        messageShow.showProgress();
        db.collection("Solicitudes")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        messageShow.dismissProgress();
                        return;
                    }

                    List<solicitud> solicitudList = new ArrayList<>();
                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        solicitud mas = doc.toObject(solicitud.class).withId(doc.getId());
                        solicitudList.add(mas);
                    }
                    mSolicitud = new AdapterSolicitud(solicitudList, acction);
                    messageShow.dismissProgress();
                    mRecyclerView.setAdapter(mSolicitud);
                });
    }

    @Override
    public void receivePublication(String id) {
        Intent intent = new Intent(SolicitudesAdminActivity.this, DetalleSolicitudActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }

    void init() {
        acction = this;
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        mRecyclerView = findViewById(R.id.rv_solicitudes);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}