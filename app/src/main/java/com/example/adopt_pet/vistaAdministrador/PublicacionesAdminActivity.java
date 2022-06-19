package com.example.adopt_pet.vistaAdministrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adopt_pet.Adapter.AdapterPublication;
import com.example.adopt_pet.Adapter.receiveId;
import com.example.adopt_pet.R;
import com.example.adopt_pet.models.mascota;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PublicacionesAdminActivity extends AppCompatActivity implements receiveId {

    RecyclerView mRecyclerView;
    MessageShow messageShow;
    FloatingActionButton addPublication;
    AdapterPublication mPublication;
    FirebaseFirestore db;
    receiveId acction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_admin);

        init();
        recuperarPublicaciones();

        addPublication.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_right_animation, R.anim.exit_right_animation, R.anim.enter_right_animation, R.anim.exit_right_animation);
            transaction.addToBackStack(null);
            FragmentPublicacion fragmentPublication = FragmentPublicacion.newInstance();
            transaction.add(R.id.fragment_container, fragmentPublication, "fragment_publication").commit();
        });
    }

    void recuperarPublicaciones() {
        messageShow.showProgress();
        db.collection("Publicaciones")
                .whereEqualTo("state", true)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        messageShow.dismissProgress();
                        return;
                    }

                    List<mascota> mascotaList = new ArrayList<>();
                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        mascota mas = doc.toObject(mascota.class).withId(doc.getId());
                        mascotaList.add(mas);
                    }
                    messageShow.dismissProgress();
                    mPublication = new AdapterPublication(mascotaList, acction);
                    mRecyclerView.setAdapter(mPublication);
                });
    }

    void init() {
        acction = this;
        messageShow = new MessageShow(getSupportFragmentManager());
        messageShow.init();
        db = FirebaseFirestore.getInstance();
        addPublication = findViewById(R.id.add_publication);
        mRecyclerView = findViewById(R.id.rv_publications);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void receivePublication(String id) {
        Intent intent = new Intent(PublicacionesAdminActivity.this, DetallePublicacionesActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }
}