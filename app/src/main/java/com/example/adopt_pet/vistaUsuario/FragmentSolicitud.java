package com.example.adopt_pet.vistaUsuario;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.constants;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.solicitud;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FragmentSolicitud extends Fragment {

    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference docRef;
    String uid;
    MessageShow messageShow;
    preferenceManager manager;
    TextView nameS;
    TextView razaS;
    TextView tipoS;
    TextView estadoT;
    TextView estadoS;
    TextView mensajeAp;
    TextView mensajeEl;
    ImageView image_mascota;
    Zoomy.Builder builder;
    Button cancelar;
    Button eliminar;
    solicitud sol;
    LinearLayout linearLayout3;

    public FragmentSolicitud() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_solicitud, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getSolicitud();

        builder.target(image_mascota)
                .animateZooming(false)
                .enableImmersiveMode(false);
        builder.register();

        eliminar.setOnClickListener(view1 -> eliminarSolicitud());
        cancelar.setOnClickListener(view1 -> eliminarSolicitud());
    }

    void eliminarSolicitud() {

        if (sol != null) {
            messageShow.showProgress();
            db.collection("Solicitudes").document(sol.getUsuarioID())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        messageShow.dismissProgress();
                        linearLayout3.setVisibility(View.GONE);
                        manager.putString(constants.SOLOCITUD_USUARIO, "");
                        Toast.makeText(requireActivity(), "Eliminado con éxito!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        messageShow.dismissProgress();
                        Toast.makeText(requireActivity(), "Error al eliminar la solicitud", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void getSolicitud() {

        messageShow.showProgress();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                linearLayout3.setVisibility(View.GONE);
                messageShow.dismissProgress();
            }

            if (snapshot != null && snapshot.exists()) {
                sol = snapshot.toObject(solicitud.class);
                assert sol != null;
                manager.putString(constants.SOLOCITUD_USUARIO, sol.getEstado());
                setData();
                messageShow.dismissProgress();
                linearLayout3.setVisibility(View.VISIBLE);
            } else {
                manager.putString(constants.SOLOCITUD_USUARIO, "");
                messageShow.dismissProgress();
                linearLayout3.setVisibility(View.GONE);
            }
        });
    }

    void setData() {

        if (sol != null) {
            nameS.setText(sol.getNombreMascota());
            razaS.setText(sol.getRazaMascota());
            tipoS.setText(sol.getTipoMascota());
            estadoT.setText(sol.getInfo());
            estadoS.setText(sol.getEstado());

            switch (sol.getEstado()) {
                case "Aprobada":
                    mensajeAp.setVisibility(View.VISIBLE);
                    cancelar.setVisibility(View.GONE);
                    break;
                case "Rechazada":
                    cancelar.setVisibility(View.GONE);
                    mensajeEl.setVisibility(View.VISIBLE);
                    eliminar.setVisibility(View.VISIBLE);
                    break;
                case "Pendiente":
                    mensajeEl.setVisibility(View.GONE);
                    mensajeAp.setVisibility(View.GONE);
                    eliminar.setVisibility(View.GONE);
                    cancelar.setVisibility(View.VISIBLE);
                    break;
            }

            if (sol.getFoto() != null && !Objects.equals(sol.getFoto(), "")) {
                Picasso.get()
                        .load(sol.getFoto())
                        .placeholder(R.color.black)
                        .error(R.color.purple_200)
                        .into(image_mascota, new Callback() {
                            @Override
                            public void onSuccess() {
                                image_mascota.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {
                                image_mascota.setVisibility(View.VISIBLE);
                                image_mascota.setImageResource(R.drawable.portada);
                            }
                        });
            }
            linearLayout3.setVisibility(View.VISIBLE);
        } else {
            linearLayout3.setVisibility(View.GONE);
        }
    }

    void init(@NonNull View view) {
        manager = new preferenceManager(requireActivity());
        messageShow = new MessageShow(requireActivity().getSupportFragmentManager());
        messageShow.init();
        builder = new Zoomy.Builder(requireActivity());
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("Solicitudes").document(uid);
        nameS = view.findViewById(R.id.nameS);
        razaS = view.findViewById(R.id.razaS);
        tipoS = view.findViewById(R.id.tipoS);
        estadoT = view.findViewById(R.id.estadoT);
        estadoS = view.findViewById(R.id.estadoS);
        mensajeAp = view.findViewById(R.id.mensajeAp);
        cancelar = view.findViewById(R.id.cancelar);
        mensajeEl = view.findViewById(R.id.mensajeEl);
        eliminar = view.findViewById(R.id.eliminar);
        image_mascota = view.findViewById(R.id.image_mascota);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
    }
}