package com.example.adopt_pet.vistaAdministrador;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.adopt_pet.R;
import com.example.adopt_pet.ayudantes.validaciones;
import com.example.adopt_pet.mostrarMensajes.MessageShow;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentPublicacion extends Fragment {

    TextInputLayout nombreM;
    TextInputLayout razaM;
    TextInputLayout edadM;
    TextInputLayout contactoM;
    TextInputLayout descripcionM;
    ImageView publicationImage;
    RadioButton perro;
    RadioButton gato;
    ActivityResultLauncher<String> mTakePhoto;
    Button potsPublication;
    MessageShow show;
    Uri uri = null;
    validaciones mValidations;

    public FragmentPublicacion() {
        // Required empty public constructor
    }

    @NonNull
    public static FragmentPublicacion newInstance() {
        return new FragmentPublicacion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicacion, container, false);
        init(view);
        publicationImage.setOnClickListener(view1 -> mTakePhoto.launch("image/*"));
        potsPublication.setOnClickListener(view12 -> validate());
        return view;
    }

    private void validate() {

        if (uri != null) {
            if (!mValidations.handleValidate(nombreM) | !mValidations.handleValidate(razaM) | !mValidations.handleValidate(edadM) |
                    !mValidations.handleValidate(contactoM) | !mValidations.handleValidate(descripcionM)) {
                return;
            }
        } else {
            Toast.makeText(getActivity(), R.string.select_photo, Toast.LENGTH_LONG).show();
            return;
        }
        uploadPhoto(uri);
    }

    void uploadPhoto(Uri uri) {
        show.showProgress();
        String name = System.currentTimeMillis() + ".jpg";
        StorageReference upload = FirebaseStorage.getInstance().getReference().child("adopt/" + name);
        upload.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            upload.getDownloadUrl().addOnSuccessListener(uri1 -> {
                String url = uri1.toString();
                uploadPost(getFormData(url, name));
            });
        }).addOnFailureListener(e -> {
            show.dismissProgress();
            show.showMessageV(e.getMessage());
        });
    }

    public void uploadPost(Map<String, Object> publication) {
        FirebaseFirestore.getInstance().collection("Publicaciones").add(publication).addOnSuccessListener(documentReference -> {
            show.dismissProgress();
            requireActivity().onBackPressed();
        }).addOnFailureListener(e -> {
            show.dismissProgress();
            show.showMessageV(e.getMessage());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    void init(@NonNull View view) {

        mValidations = new validaciones(requireActivity());
        show = new MessageShow(requireActivity().getSupportFragmentManager());
        show.init();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        uri = result;
                        publicationImage.setImageURI(result);
                    }
                }
        );

        potsPublication = view.findViewById(R.id.publicar);
        nombreM = view.findViewById(R.id.nombreM);
        razaM = view.findViewById(R.id.razaM);
        edadM = view.findViewById(R.id.edadM);
        contactoM = view.findViewById(R.id.contactoM);
        descripcionM = view.findViewById(R.id.descripcionM);
        descripcionM = view.findViewById(R.id.descripcionM);
        perro = view.findViewById(R.id.rbPerro);
        gato = view.findViewById(R.id.rbGato);
        publicationImage = view.findViewById(R.id.publication_image);
    }

    Map<String, Object> getFormData(String urlImage, String path) {

        String tipo;

        if (perro.isChecked()) tipo = "Perro";
        else tipo = "Gato";

        Map<String, Object> setPublication = new HashMap<>();
        setPublication.put("nombre", Objects.requireNonNull(nombreM.getEditText()).getText().toString().trim());
        setPublication.put("raza", Objects.requireNonNull(razaM.getEditText()).getText().toString().trim());
        setPublication.put("edad", Objects.requireNonNull(edadM.getEditText()).getText().toString().trim());
        setPublication.put("contacto", Objects.requireNonNull(contactoM.getEditText()).getText().toString().trim());
        setPublication.put("descripcion", Objects.requireNonNull(descripcionM.getEditText()).getText().toString().trim());
        setPublication.put("estado", "En busca de hogar");
        setPublication.put("fechaPublication", DateFormat.getDateInstance().format(new Date()));
        setPublication.put("tipo", tipo);
        setPublication.put("foto", urlImage);
        setPublication.put("path", "adopt/" + path);
        setPublication.put("state",true);
        setPublication.put("adopcion","no");
        return setPublication;
    }
}