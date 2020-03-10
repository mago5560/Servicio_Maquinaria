package com.pantaleon.serviciomaquinaria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.pantaleon.serviciomaquinaria.Adapter.RevisionConsultaAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.RevisionTecleoAdapter;
import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.Controler.LoginControler;
import com.pantaleon.serviciomaquinaria.Controler.PilotoControler;
import com.pantaleon.serviciomaquinaria.Controler.RevisionControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.Customs.Globals;

import java.util.ArrayList;

public class MactyPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
, RevisionTecleoAdapter.OnItemClickListener{

    Intent intent;
    ActionBarDrawerToggle toggle;
    View navHeader;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Functions util;
    Globals vars = Globals.getInstance();

    LoginControler loginControler;

    //Var GRD
    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    ArrayList<RevisionTecleo> arrayList;
    RevisionTecleoAdapter mAdapter;
    RevisionControler revisionControler;
    //----------------


    PilotoControler pilotoControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.macty_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewxByIds();
        actions();
        existLogin();
        buscar();
    }

    private void findViewxByIds() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Servicios Locales");
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        util = new Functions();


        arrayList = new ArrayList<>();
        revisionControler = new RevisionControler(this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        grdDatos = (RecyclerView) findViewById(R.id.grdDatos);
        grdDatos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatos.setLayoutManager(llm);

        loginControler = new LoginControler(this);
    }


    private void actions() {
        ((FloatingActionButton) findViewById(R.id.fabAgregar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grabarRevision("","",0);
            }
        });

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

    }

    private void existLogin() {

        if (!loginControler.existLogin()) {
            intent = new Intent().setClass(this, MactyLogin.class);
            startActivity(intent);
            finish();
        } else if (loginControler.loginVencido()) {
            intent = new Intent().setClass(this, MactyLogin.class);
            startActivity(intent);
            finish();
        } else {
            setNavHeader();
        }
    }


    private void buscar(){
        grdDatos.setScrollingTouchSlop(0);
        mAdapter = new RevisionTecleoAdapter(arrayList, this, this);
        revisionControler = new RevisionControler(this, mAdapter, grdDatos, refreshLayout);
        revisionControler.buscar(2,"");
        ((TextView) findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }


    private void dialogEliminarRegistro(final String Id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getDecorView().getRootView().getContext());
        builder.setCancelable(false);
        builder.setTitle("Eliminar Registro");
        builder.setMessage("Si elimina la revision No. " + Id +", ya no podra recuperar la informacion.\n¿Desea eliminar la revision?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                eliminarRevision(Id);
            }
        });
        builder.show();
    }

    private  void eliminarRevision(String Id){
        revisionControler = new RevisionControler(this);
        revisionControler.eliminarRevision(Id);
        buscar();
    }

    private  void transferirRevision(RevisionTecleo cls){
        grdDatos.setScrollingTouchSlop(0);
        mAdapter = new RevisionTecleoAdapter(arrayList, this, this);
        revisionControler = new RevisionControler(this, mAdapter, grdDatos, refreshLayout);
        revisionControler.transferirReivision(cls,2);
    }

    @Override
    public void onClick(RevisionTecleoAdapter.ItemAdapterViewHolder holder, int position, int options) {
        switch (options){
            case 1: //Click CV
                grabarRevision(String.valueOf(mAdapter.info.get(position).getId()),mAdapter.info.get(position).getFecha(),mAdapter.info.get(position).getTransferido() );
                break;
            case 2://Click Transferir
                transferirRevision(mAdapter.info.get(position));
                break;
            case 3://Click Eliminar
                dialogEliminarRegistro(String.valueOf(mAdapter.info.get(position).getId()));
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Menu and Acctions">
    private void setNavHeader() {
        navHeader = navigationView.getHeaderView(0);
        ((TextView) navHeader.findViewById(R.id.lblInicialesMenu)).setText(loginControler.getIniciales());
        ((TextView) navHeader.findViewById(R.id.lblVersion)).setText(util.getVersion(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        toggle.syncState();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        toggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_grabar_revision:
                grabarRevision("","",0);
                break;
            case R.id.nav_consultas_servicios:
                consultaRevision();
                break;
            case R.id.nav_servicio:
                grabarMaestroServicios();
                break;
            case R.id.nav_vehiculo:
                getVehiculo();
                break;
            case R.id.nav_piloto:
                getPiloto();
                break;
            case R.id.nav_actualizacion:
                DescargarActualizacionApp();
                break;
            case R.id.nav_compartir_api:
                CompartirAplicacion();
                break;
            case R.id.mnucerrarsesion:
                loginControler.cerrarSesion();
                break;
            default:
                util.msgSnackBar("Opcion en mantenimiento", this);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void grabarRevision(String Id,String Fecha, int Transferido) {
        intent = new Intent().setClass(this, MactyRevisionTecleo.class);
        if(!Id.isEmpty() ) {
            intent .putExtra("id",Id);
            intent.putExtra("fecha",Fecha);
            intent.putExtra("transferido",Transferido);
        }
        startActivity(intent);
        finish();
    }

    private void grabarMaestroServicios() {
        intent = new Intent().setClass(this, MactyServicio.class);
        startActivity(intent);
        finish();
    }

    private void getVehiculo(){
        pilotoControler = new PilotoControler(this);
        pilotoControler.getVehiculo();
    }
    private void getPiloto(){
        pilotoControler = new PilotoControler(this);
        pilotoControler.getPiloto();
    }

    private void CompartirAplicacion() {

        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, vars.getLINK_APP());
        startActivity(Intent.createChooser(intent, "Compartir " + vars.getNameApi()));

    }

    private void DescargarActualizacionApp() {

        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(vars.getLINK_APP()));
        startActivity(intent);

    }

    private void consultaRevision() {
        intent = new Intent().setClass(this, MactyRevisionConsulta.class);
        startActivity(intent);
        finish();
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="(Grant permits)">
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int  MY_PERMISSIONS_REQUEST_CAMERA= 2;
    private static  final int MY_PERMISSIONS_REQUEST_INTETNER = 3;
    private static  final int MY_PERMISSIONS_REQUEST_GPS = 4;

    private void permisosAPI(){
        int STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int GPS = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);

        if (STORAGE != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }

        if (CAMERA!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }


        if(GPS != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_GPS);
            }
        }

        if (STORAGE == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }

        if (CAMERA == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }


        if(GPS == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_GPS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Escritura en memoria es requerida",this);
                }return;
            } case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {
                    util.msgSnackBar("El Permiso de Camara es requerida",this);
                }return;
            }case MY_PERMISSIONS_REQUEST_INTETNER:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permisosAPI();
                }else{
                    util.msgSnackBar("El Permiso de Internet es requerida",this);
                }return;
            }case MY_PERMISSIONS_REQUEST_GPS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permisosAPI();
                }else{
                    util.msgSnackBar("El Permiso de GPS es requerida",this);
                }return;
            }

        }
    }

    // </editor-fold>

}

