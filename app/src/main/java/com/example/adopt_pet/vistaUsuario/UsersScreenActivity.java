package com.example.adopt_pet.vistaUsuario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.adopt_pet.Adapter.viewPagerAdapter;
import com.example.adopt_pet.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UsersScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_screen);

        viewPager();
    }

    void viewPager() {

        ViewPager2 vp_horizontal = findViewById(R.id.vp_horizontal);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPagerAdapter mPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mPagerAdapter.addFragment(new FragmentGato());
        mPagerAdapter.addFragment(new FragmentPerro());
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
                    tab.setIcon(R.drawable.solicitud);
                    break;
                case 3:
                    tab.setIcon(R.drawable.usuario);
                    break;
            }
        }).attach();
    }
}