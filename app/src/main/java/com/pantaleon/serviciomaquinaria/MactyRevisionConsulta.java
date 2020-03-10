package com.pantaleon.serviciomaquinaria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.pantaleon.serviciomaquinaria.Adapter.RevisionConsultaAdapter;
import com.pantaleon.serviciomaquinaria.Class.RevisionConsulta;
import com.pantaleon.serviciomaquinaria.Controler.RevisionControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;

import java.util.ArrayList;

public class MactyRevisionConsulta extends AppCompatActivity implements RevisionConsultaAdapter.OnItemClickListener{
    Intent intent;

    //Var GRD
    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    ArrayList<RevisionConsulta> arrayList;
    RevisionConsultaAdapter mAdapter;
    RevisionControler revisionControler;
    //----------------

    Functions util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_revision_consulta);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Consulta RevisionConsulta");
        findViewsByIds();
        actions();
    }

    private void findViewsByIds(){
        util = new Functions();
        arrayList = new ArrayList<>();
        revisionControler = new RevisionControler(this);


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        grdDatos = (RecyclerView) findViewById(R.id.grdDatos);
        grdDatos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatos.setLayoutManager(llm);


    }

    private void actions(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscar();
            }
        });

        ((SearchView) findViewById(R.id.txtBuscador)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(mAdapter != null ) {
                    mAdapter.getFilter().filter(s);
                }
                return false;
            }

        });

        ((ImageView) findViewById(R.id.imgvwbuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SearchView) findViewById(R.id.txtBuscador)).getVisibility() == View.VISIBLE) {
                    ((SearchView) findViewById(R.id.txtBuscador)).setVisibility(View.GONE);
                    ((ImageView) v).setImageResource(R.drawable.buscar);
                    ((SearchView) findViewById(R.id.txtBuscador)).setQuery("", false);
                } else {
                    ((SearchView) findViewById(R.id.txtBuscador)).setVisibility(View.VISIBLE);
                    ((ImageView) v).setImageResource(R.drawable.backspace);
                    ((SearchView) findViewById(R.id.txtBuscador)).requestFocus();
                }
            }
        });


        ((ImageButton) findViewById(R.id.imgbtnBuscar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });
    }


    @Override
    public void onClick(RevisionConsultaAdapter.ItemAdapterViewHolder holder, int position, int options) {
        util.msgSnackBar(mAdapter.info.get(position).getEstado(),this);
    }

    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    private void buscar() {
        if(ValidarCampos()) {
            grdDatos.setScrollingTouchSlop(0);
            mAdapter = new RevisionConsultaAdapter(arrayList, this, this);
            revisionControler = new RevisionControler(this, mAdapter, grdDatos, refreshLayout);
            revisionControler.getRevisionOnLine(Integer.valueOf(((EditText) findViewById(R.id.txtIdVehiculo)).getText().toString())  );
            ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
        }
    }

    private  boolean ValidarCampos(){

        if(util.validarCampoVacio(((EditText) findViewById(R.id.txtIdVehiculo)))){
            refreshLayout.setRefreshing(false);
            return false;
        }

        return  true;
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
