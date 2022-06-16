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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.zoomy.Zoomy;
import com.example.adopt_pet.R;
import com.example.adopt_pet.autenticacion.validarRolActivity;
import com.example.adopt_pet.ayudantes.constants;
import com.example.adopt_pet.ayudantes.preferenceManager;
import com.example.adopt_pet.models.solicitud;
import com.example.adopt_pet.models.usuario;
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
    ImageView image_mascota;
    Zoomy.Builder builder;
    Button cancelar;

    //recycler_solicitud
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

    }

    private void getSolicitud() {

        messageShow.showProgress();

        docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                messageShow.dismissProgress();
            }

            if (snapshot != null && snapshot.exists()) {
                solicitud sol = snapshot.toObject(solicitud.class);
                assert sol != null;
                manager.putString(constants.SOLOCITUD_USUARIO, sol.getEstado());
                setData(sol);
                messageShow.dismissProgress();
            }
        });
    }

    void setData(solicitud sol) {

        if (sol != null) {
            nameS.setText(sol.getNombreMascota());
            razaS.setText(sol.getRazaMascota());
            tipoS.setText(sol.getTipoMascota());
            estadoT.setText(sol.getInfo());
            estadoS.setText(sol.getEstado());

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
        image_mascota = view.findViewById(R.id.image_mascota);
    }
}