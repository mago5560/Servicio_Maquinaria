package com.pantaleon.serviciomaquinaria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pantaleon.serviciomaquinaria.Adapter.ServicioAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.ServicioDetalleAdapter;
import com.pantaleon.serviciomaquinaria.Class.Servicio;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Controler.ServicioControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;

import java.util.ArrayList;

public class MactyServicio extends AppCompatActivity implements ServicioAdapter.OnItemClickListener
        ,ServicioDetalleAdapter.OnItemClickListener{

    Intent intent;

    //Var GRD
    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    ServicioControler controler;
    ArrayList<Servicio> arrayList;
    ServicioAdapter mAdapter;
    //----------------------------
    RecyclerView grdDatosDetalle;
    SwipeRefreshLayout refreshLayoutDetalle;
    ArrayList<ServicioDetalle> arrayDetalleList;
    ServicioDetalleAdapter mDetalleAdapter;
    //----------------

    Functions util;
    int IdServicio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_servicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Maestro de Servicios");
        findViewsByIds();
        actions();
        buscar();
    }


    private void findViewsByIds() {
        util = new Functions();
        arrayList = new ArrayList<>();
        controler = new ServicioControler(this);
        arrayDetalleList = new ArrayList<>();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        grdDatos = (RecyclerView) findViewById(R.id.grdDatos);
        grdDatos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatos.setLayoutManager(llm);


    }

    private void actions() {
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
                if (mAdapter != null) {
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


        ((FloatingActionButton) findViewById(R.id.fabDescargar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargar();
            }
        });
    }

    @Override
    public void onClick(ServicioAdapter.ItemAdapterViewHolder holder, int position, int options) {
        this.IdServicio = Integer.valueOf( mAdapter.info.get(position).getIdServicio());
        dialogDetalle(mAdapter.info.get(position).getDescripcion());

    }

    private void descargar() {
        grdDatos.setScrollingTouchSlop(0);
        mAdapter = new ServicioAdapter(arrayList, this, this);
        controler = new ServicioControler(this, mAdapter, grdDatos, refreshLayout);
        controler.getServicios();
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }


    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    private void buscar() {
        grdDatos.setScrollingTouchSlop(0);
        mAdapter = new ServicioAdapter(arrayList, this, this);
        controler = new ServicioControler(this, mAdapter, grdDatos, refreshLayout);
        controler.buscar(1,"","");
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="(dialog Detalle Servicios)">
    AlertDialog alertDialogDetalle;
    private void dialogDetalle(String Categoria) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext());
        builder.setCancelable(false);

        final LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.content_consulta, null);
        findViewsByIdsDialogCombo(dialogLayout);
        actionDialogCombo(dialogLayout);

        //builder.setTitle("Seleccione El Registro A Usar");
        builder.setTitle(Categoria);

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(dialogLayout);
        alertDialogDetalle = builder.show();
    }

    private void findViewsByIdsDialogCombo(View viewLocal) {
        refreshLayoutDetalle = (SwipeRefreshLayout) viewLocal.findViewById(R.id.swipeRefresh);
        grdDatosDetalle = (RecyclerView) viewLocal.findViewById(R.id.grdDatos);
        grdDatosDetalle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(viewLocal.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatosDetalle.setLayoutManager(llm);
        buscarServicioDetalle(viewLocal);
    }

    private void buscarServicioDetalle(View viewLocal){
        mDetalleAdapter = new ServicioDetalleAdapter(arrayDetalleList, this, this,1);
        controler = new ServicioControler(viewLocal.getContext(), grdDatosDetalle, refreshLayoutDetalle, mDetalleAdapter);
        controler.buscar(2,String.valueOf(IdServicio),"");
        ((TextView) viewLocal.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(ServicioDetalleAdapter.ItemAdapterViewHolder holder, int position, int options) {
        util.msgToast(mDetalleAdapter.info.get(position).getDescripiconRevision(),this);
    }

    private void actionDialogCombo(final View viewLocal) {
        ((ImageView) viewLocal.findViewById(R.id.imgvwbuscador)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).getVisibility() == View.VISIBLE ){
                    ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).setVisibility(View.GONE);
                    ((ImageView) v).setImageResource(R.drawable.buscar);
                    ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).setQuery("",false);
                }else{
                    ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).setVisibility(View.VISIBLE);
                    ((ImageView) v).setImageResource(R.drawable.backspace);
                    ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).requestFocus();
                }
            }
        });

        ((SearchView) viewLocal.findViewById(R.id.txtBuscador)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (mDetalleAdapter != null) {
                    mDetalleAdapter.getFilter().filter(s);
                }
                return false;
            }
        });

        refreshLayoutDetalle.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscarServicioDetalle(viewLocal);
            }
        });

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
