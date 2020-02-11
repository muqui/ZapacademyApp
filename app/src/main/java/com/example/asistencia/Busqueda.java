package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.adapter.ViewPagerAdapter;
import com.example.model.Event;
import com.example.model.Token;
import com.google.android.material.tabs.TabLayout;

public class Busqueda extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Token token;
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        token = (Token) getIntent().getSerializableExtra("token");
        event = (Event) getIntent().getSerializableExtra("event");
        Log.d("NUeva Busqueda", ""+ token.getToken());
        Log.d("Nueva Busquesa", ""+ token.getUsuario().getId());

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.myViewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager( ViewPager viewPager){

        //Fragment busqueda por curp
        Fragment fragmentCurp = new BusquedaCURPFragment();
        Bundle bundleFragment = new Bundle();
        bundleFragment.putSerializable("event", event);
        bundleFragment.putSerializable("token", token);
        fragmentCurp.setArguments(bundleFragment);

        //Fragment por Datos
        Fragment fragmentDatos = new BusquedaDatosFragment();
        Bundle bundleFragmentDatos = new Bundle();
        bundleFragmentDatos.putSerializable("event", event);
        bundleFragmentDatos.putSerializable("token", token);
        fragmentDatos.setArguments(bundleFragmentDatos);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fragmentCurp, "CURP");  // Busqueda mediante CURP

        viewPagerAdapter.addFragment( fragmentDatos, "Datos personales");  //Busqueda mediaten datos personales.

        viewPager.setAdapter(viewPagerAdapter);

    }
}
