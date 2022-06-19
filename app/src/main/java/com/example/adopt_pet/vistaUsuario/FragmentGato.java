package com.example.adopt_pet.vistaUsuario;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adopt_pet.Adapter.AdapterPublication;
import com.example.adopt_pet.Adapter.receiveId;
import com.example.adopt_pet.R;
import com.example.adopt_pet.models.mascota;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.example.adopt_pet.vistaAdministrador.PublicacionesAdminActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentGato extends Fragment implements receiveId {

    FirebaseFirestore db;
    AdapterPublication mPublication;
    RecyclerView mRecyclerView;
    MessageShow messageShow;
    receiveId acction;

    public FragmentGato() {
        // Required empty public constructor
    }

    @NonNull
    public static FragmentGato newInstance() {
        return new FragmentGato();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getGatos();
    }

    void getGatos() {
        messageShow.showProgress();
        db.collection("Publicaciones")
                .whereEqualTo("tipo", "Gato")
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gato, container, false);
    }

    void init(@NonNull View view) {
        acction = this;
        messageShow = new MessageShow(requireActivity().getSupportFragmentManager());
        messageShow.init();
        mRecyclerView = view.findViewById(R.id.recycler_gato);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void receivePublication(String id) {
        Intent intent = new Intent(requireActivity(), mascotaDetalleActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }
}