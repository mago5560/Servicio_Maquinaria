package com.pantaleon.serviciomaquinaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pantaleon.serviciomaquinaria.Fragments.FragmDetalle;
import com.pantaleon.serviciomaquinaria.Fragments.FragmEncabezado;

public class MactyRevisionTecleo extends AppCompatActivity implements FragmEncabezado.OnFragmClickListener {
    Intent intent;
    String Id="";
    String Fecha="";
    int Transferido=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_revision_tecleo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Tecleo de Revision");
        getIntentExtra();
        findViewsByIds();
        actions();
    }


    private void getIntentExtra() {
        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        if (bundle != null) {
            Id = bundle.getString("id");
            Fecha = bundle.getString("fecha");
            Transferido = bundle.getInt("transferido");
        }
    }

    private void findViewsByIds() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FragmEncabezado.newInstance(Id)).commit();
    }

    private void actions() {

        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment ;
                switch (item.getItemId()){
                    case R.id.nav_mnu_detalle:
                        selectFragment = FragmDetalle.newInstance(Id,Fecha,Transferido);
                        break;
                    default:
                        selectFragment = FragmEncabezado.newInstance(Id);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();

                return true;
            }
        });
    }



    // <editor-fold defaultstate="collapsed" desc="Eventos Framg">


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof FragmEncabezado){
            FragmEncabezado fragmEncabezado = (FragmEncabezado) fragment;
            fragmEncabezado.setOnFragmClickListener(this);
        }
    }

    @Override
    public void onClick(String ID, String Fecha) {
        this.Id = ID;
        this.Fecha = Fecha;
        if(!ID.isEmpty()) {
            ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.nav_mnu_detalle);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="(Go Home)">
    @Override
    public void onBackPressed() {
        goHome();
        super.onBackPressed();
    }

    private void goHome() {
        intent = new Intent().setClass(this, MactyPrincipal.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // </editor-fold>

}
