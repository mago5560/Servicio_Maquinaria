package com.pantaleon.serviciomaquinaria.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pantaleon.serviciomaquinaria.Adapter.RevisionConsultaAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.RevisionTecleoAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.ServicioDetalleAdapter;
import com.pantaleon.serviciomaquinaria.Class.Login;
import com.pantaleon.serviciomaquinaria.Class.RevisionConsulta;
import com.pantaleon.serviciomaquinaria.Class.RevisionConsultaList;
import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalleList;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.Customs.Globals;
import com.pantaleon.serviciomaquinaria.Model.DbHandler;
import com.pantaleon.serviciomaquinaria.Model.PantaleonWS;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RevisionControler {

    Context context;
    RevisionConsultaAdapter mAdapter;
    RecyclerView grdDatos;
    SwipeRefreshLayout refreshLayout;
    ArrayList<RevisionConsulta> arrayList;
    ProgressDialog pDialog;
    Functions util;
    Globals vars;
    DbHandler dbHandler;
    LoginControler loginControler;

    ServicioDetalleAdapter mDetalleAdapter;



    TextView lblFecha,lblNombrePiloto, lblLicencia, lblEdad, lblFechaVenceLiciencia, lblPlaca;
    EditText txtCodPiloto, txtTC,txtIdVehiculo;


    RevisionTecleoAdapter revisionTecleoAdapter;

    public RevisionControler(Context context) {
        this.context = context;
        init();
    }

    public RevisionControler(Context context, RevisionConsultaAdapter mAdapter, RecyclerView grdDatos, SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.mAdapter = mAdapter;
        this.grdDatos = grdDatos;
        this.refreshLayout = refreshLayout;
        init();
    }

    public RevisionControler(Context context, RevisionTecleoAdapter revisionTecleoAdapter, RecyclerView grdDatos, SwipeRefreshLayout refreshLayout) {
        this.context = context;
        this.grdDatos = grdDatos;
        this.refreshLayout = refreshLayout;
        this.revisionTecleoAdapter = revisionTecleoAdapter;
        init();
    }

    public RevisionControler(Context context, TextView lblNombrePiloto, TextView lblLicencia, TextView lblEdad, TextView lblFechaVenceLiciencia, TextView lblPlaca, EditText txtCodPiloto, EditText txtTC , TextView lblFecha, EditText txtIdVehiculo) {
        this.context = context;
        this.lblNombrePiloto = lblNombrePiloto;
        this.lblLicencia = lblLicencia;
        this.lblEdad = lblEdad;
        this.lblFechaVenceLiciencia = lblFechaVenceLiciencia;
        this.lblPlaca = lblPlaca;
        this.txtCodPiloto = txtCodPiloto;
        this.txtTC = txtTC;
        this.lblFecha = lblFecha;
        this.txtIdVehiculo = txtIdVehiculo;
        init();
    }

    private void init(){
        arrayList  = new ArrayList<>();
        util = new Functions();
        vars = Globals.getInstance();
        loginControler = new LoginControler(context);
        dbHandler = new DbHandler(context,null,null,1);
        createRetrofit();
    }


    private void createRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public int insertEncabezado(RevisionTecleo cls){
        long id=0;
        id = dbHandler.insertRevision(cls);
        return (int)id;
    }

    public void eliminarRevision(String Id){
        dbHandler.deleteRevision(Id);
    }


    public void transferirReivision(RevisionTecleo cls,int TipoBusqueda){
        this.TipoBusqueda = TipoBusqueda;
        //setRevisionEncabezado(cls);
        asyncEnviarRevision = new AsyncEnviarRevision();
        asyncEnviarRevision.execute(String.valueOf(cls.getId()));
    }

    public int insertupdateDetalle(ServicioDetalle cls){
        long id=0;
        if(cls.getIdRevisionDetalle()== 0) {
            id = dbHandler.insertRevisionDetalle(cls);
        }else{
            id = Integer.valueOf(cls.getIdRevisionDetalle());
            dbHandler.updateRevisionDetalle(cls);
        }
        return (int) id;
    }


    // <editor-fold defaultstate="collapsed" desc="(Enviar Revision)">

    AsyncEnviarRevision asyncEnviarRevision;
    private class AsyncEnviarRevision extends AsyncTask<String, Void, Integer> {
        private ProgressDialog pDialog;
        String v_mensaje;

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost post = new HttpPost(vars.getWS_URL()+ "/creaencabezado");
                String  v_cod_boleta_revision = strings[0];
                RevisionTecleo obj =  (RevisionTecleo) dbHandler.selectRevision(v_cod_boleta_revision).get(0);
                JSONObject dato = new JSONObject();
                dato.put("datitos", dbHandler.selectServicioDetalleJSON(v_cod_boleta_revision));
                dato.put("cod_boleta_revision", obj.getId());
                dato.put("fecha_revision",util.formatDateUSA(obj.getFecha()));
                dato.put("cod_vehiculo", obj.getIdVehiculo());
                dato.put("nombre_piloto", obj.getNombrePiloto());
                dato.put("edad_piloto", obj.getEdadPiloto());
                dato.put("cod_piloto", obj.getCodPiloto());
                dato.put("tc", obj.getTC());
                dato.put("usuario", loginControler.getPreferenceLogin().getUsuario());
                dato.put("procesado", "S");
                new StringEntity(dato.toString());
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("datos", dato.toString()));
                Log.e("mainToPost", "mainToPost" + nameValuePairs.toString());
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
                post.setEntity(urlEncodedFormEntity);
                byte[] buffer = EntityUtils.toByteArray(httpClient.execute(post).getEntity());
                String respStr = new String(buffer);
                this.v_mensaje = respStr;
                String str = "";
                String str2 = "";
                Integer valueOf2 = Integer.valueOf(0);
                JSONObject jsonResponse = new JSONObject(respStr);
                String v_error = jsonResponse.getString("error");
                String v_message = jsonResponse.getString("message");
                Integer v_identificador_bdo = Integer.valueOf(jsonResponse.getInt("identificadorbdo"));
                Log.e("jrespons","jrespons:" +  jsonResponse.toString());
                if (!v_message.equals("OK")) {
                    dbHandler.updateRevisionTransferido("0",v_cod_boleta_revision,String.valueOf(v_identificador_bdo));
                    return Integer.valueOf(-1);

                }
                dbHandler.updateRevisionTransferido("1",v_cod_boleta_revision,String.valueOf(v_identificador_bdo));
                return Integer.valueOf(v_cod_boleta_revision);
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                Integer v_cod_boleta_revision2 = Integer.valueOf(-9999);
                return v_cod_boleta_revision2;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Enviando Informacion...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer > 0){
                util.msgToast("Revision transferido con exito....",context);
            }else{
                util.msgToast("No fue enviada la revision....",context);
            }

            buscar(TipoBusqueda,"");
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

    }

    // </editor-fold>


    //Objetos de prueba aun no funcionan.....
    public void setRevisionEncabezado(final RevisionTecleo revisionEncabezado){
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Login login = new Login();
        login = loginControler.getPreferenceLogin();
        revisionEncabezado.setUsuario(login.getUsuario());

        Call<RevisionTecleo> call = postService.insertEncabezado(revisionEncabezado);
        call.enqueue(new Callback<RevisionTecleo>() {
            @Override
            public void onResponse(Call<RevisionTecleo> call, Response<RevisionTecleo> response) {
                RevisionTecleo responseRevision = response.body();
                if(response.isSuccessful() ){
                    util.msgToast("Encabezado creado",context);
                    setRevisionDetalle(String.valueOf(revisionEncabezado.getId()));
                    dbHandler.updateRevisionTransferido("1",String.valueOf(revisionEncabezado.getId()),"0");
                }else{
                    util.mensaje("Ocurrio un problema al momento de crear el encabezado, favor de verificar nuevamente",((Activity) context)).show();
                    dbHandler.updateRevisionTransferido("0",String.valueOf(revisionEncabezado.getId()),"0");
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<RevisionTecleo> call, Throwable t) {
                util.mensajeError(t.getMessage(), ((Activity)context)).show();
                dbHandler.updateRevisionTransferido("0",String.valueOf(revisionEncabezado.getId()),"0");
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }
    public void setRevisionDetalle(final String IdEncabezado){
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        ServicioDetalleList servicioDetalleList =  new ServicioDetalleList();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);
        servicioDetalleList.setServicioDetalleList(dbHandler.selectRevisionDetalle(IdEncabezado));

        Call<ServicioDetalleList> call = postService.insertDetalle(servicioDetalleList);
        call.enqueue(new Callback<ServicioDetalleList>() {
            @Override
            public void onResponse(Call<ServicioDetalleList> call, Response<ServicioDetalleList> response) {
                ServicioDetalleList responseRevision = response.body();
                if(response.isSuccessful() ){
                    util.msgToast("Detalle creado",context);
                }else{
                    util.mensaje("Ocurrio un problema al momento de crear el encabezado, favor de verificar nuevamente",((Activity) context)).show();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<ServicioDetalleList> call, Throwable t) {
                util.mensajeError(t.getMessage(), ((Activity)context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }
    //------------------

    public void getRevisionOnLine(int Id) {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<RevisionConsultaList> call = postService.selectRevision(Id);
        call.enqueue(new Callback<RevisionConsultaList>() {
            @Override
            public void onResponse(Call<RevisionConsultaList> call, Response<RevisionConsultaList> response) {
                RevisionConsultaList models = response.body();
                for (RevisionConsulta post : models.getRevisionConsultaList()) {
                    arrayList.add(post);
                }
                mAdapter.setInfo(arrayList);
                grdDatos.setAdapter(mAdapter);
                refreshLayout.setRefreshing(false);
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RevisionConsultaList> call, Throwable t) {
                util.mensajeError(t.getMessage(), ((Activity)context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }


    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    AsyncBuscar asyncBuscar;
    int TipoBusqueda;
    public void buscar(int TipoBusqueda,String Id) {
        this.TipoBusqueda = TipoBusqueda;

        if(TipoBusqueda > 1){ grdDatos.setScrollingTouchSlop(0);}
        asyncBuscar = new AsyncBuscar();
        asyncBuscar.execute(Id);
    }

    private class AsyncBuscar extends AsyncTask<String, Void, ArrayList> {
        private ProgressDialog pDialog;

        @Override
        protected ArrayList doInBackground(String... strings) {
            ArrayList list;
            switch (TipoBusqueda){
                case 2: //revisiones locales
                    list = dbHandler.selectRevision("");
                    break;
                default: //Encabezado
                    list = dbHandler.selectRevision(strings[0]);
                    break;
            }

            return list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);
            viewConsulta(arrayList);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }


    private void viewConsulta(ArrayList arrayList) {
        switch (TipoBusqueda){
            case 2://Revisiones locales
                revisionTecleoAdapter.setInfo(arrayList);
                grdDatos.setAdapter(revisionTecleoAdapter);
                refreshLayout.setRefreshing(false);
                break;
            default: //Encabezado
                fillEncabezado((RevisionTecleo) arrayList.get(0));
                break;
        }
        if (arrayList.isEmpty()) {
            util.mensaje("No se encontraron registros, favor de descargar o ingresar la infromacion", ((Activity) context)).show();
        }
    }

    // </editor-fold>

    private void fillEncabezado(RevisionTecleo post){
        lblFecha.setText(post.getFecha());
        txtIdVehiculo.setText(post.getIdVehiculo());
        lblPlaca.setText(post.getPlaca());
        lblNombrePiloto.setText(post.getNombrePiloto());
        lblEdad.setText(post.getEdadPiloto());
        txtCodPiloto.setText(post.getCodPiloto());
        lblLicencia.setText(post.getLicenciaPiloto());
        lblFechaVenceLiciencia.setText(post.getFechaVenceLicencia());
        txtTC.setText(post.getTC());
    }

}
