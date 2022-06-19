package com.example.adopt_pet.vistaUsuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.adopt_pet.Adapter.viewPagerAdapter;
import com.example.adopt_pet.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UsersScreenActivity extends AppCompatActivity {

    FirebaseFirestore db;
    TextView numberS;
    TextView numberF;
    int cont = 0;
    int contS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_screen);
        init();
        tamanioPublicaciones();
        tamanioAdopciones();
        viewPager();
    }

    void init() {
        db = FirebaseFirestore.getInstance();
        numberS = findViewById(R.id.numberS);
        numberF = findViewById(R.id.numberF);
    }

    void tamanioPublicaciones() {

        db.collection("Publicaciones")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        cont = 0;
                        for (QueryDocumentSnapshot doc : value) {
                            Log.e("da", doc.getId());
                            cont = cont + 1;
                        }
                        numberS.setText(String.valueOf(cont));
                    }
                });
    }

    void tamanioAdopciones() {
        db.collection("Adopciones")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        contS = 0;
                        for (QueryDocumentSnapshot doc : value) {
                            Log.e("da", doc.getId());
                            contS = contS + 1;
                        }
                        numberF.setText(String.valueOf(contS));
                    }
                });
    }

    void viewPager() {
        ViewPager2 vp_horizontal = findViewById(R.id.vp_horizontal);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPagerAdapter mPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mPagerAdapter.addFragment(new FragmentGato());
        mPagerAdapter.addFragment(new FragmentPerro());
        mPagerAdapter.addFragment(new FragmentAdopciones());
        mPagerAdapter.addFragment(new FragmentSolicitud());
        mPagerAdapter.addFragment(new FragmentCuenta());

        vp_horizontal.setClipToPadding(false);
        vp_horizontal.setClipChildren(false);
        vp_horizontal.setOffscreenPageLimit(3);
        vp_horizontal.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        vp_horizontal.setAdapter(mPagerAdapter);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));

        transformer.addTransformer((page, position) -> {
            float v = 1 - Math.abs(position);
            page.setScaleY(0.8f + v * 0.2f);
        });
        vp_horizontal.setPageTransformer(transformer);

        new TabLayoutMediator(tabLayout, vp_horizontal, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.gato);
                    break;
                case 1:
                    tab.setIcon(R.drawable.labrador);
                    break;
                case 2:
                    tab.setIcon(R.drawable.gatito);
                    break;
                case 3:
                    tab.setIcon(R.drawable.solicitud);
                    break;
                case 4:
                    tab.setIcon(R.drawable.usuario);
                    break;
            }
        }).attach();
    }
}