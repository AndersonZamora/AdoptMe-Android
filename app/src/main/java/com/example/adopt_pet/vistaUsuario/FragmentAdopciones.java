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

import com.example.adopt_pet.Adapter.AdapterAdopciones;
import com.example.adopt_pet.Adapter.receiveId;
import com.example.adopt_pet.DetalleAdopcionActivity;
import com.example.adopt_pet.R;
import com.example.adopt_pet.models.adopciones;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdopciones extends Fragment implements receiveId {

    FirebaseFirestore db;
    AdapterAdopciones mAdopciones;
    RecyclerView mRecyclerView;
    MessageShow messageShow;
    receiveId acction;

    public FragmentAdopciones() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adopciones, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getAdopciones();
    }

    void init(@NonNull View view) {
        acction = this;
        messageShow = new MessageShow(requireActivity().getSupportFragmentManager());
        messageShow.init();
        mRecyclerView = view.findViewById(R.id.rv_adopciones);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        db = FirebaseFirestore.getInstance();
    }

    void getAdopciones() {
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

    @Override
    public void receivePublication(String id) {
        Intent intent = new Intent(requireActivity(), DetalleAdopcionActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }
}