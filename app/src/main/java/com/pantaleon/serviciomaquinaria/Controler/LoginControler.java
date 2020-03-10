package com.pantaleon.serviciomaquinaria.Controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.pantaleon.serviciomaquinaria.Class.Login;
import com.pantaleon.serviciomaquinaria.Class.LoginList;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.Customs.Globals;
import com.pantaleon.serviciomaquinaria.MactyLogin;
import com.pantaleon.serviciomaquinaria.MactyPrincipal;
import com.pantaleon.serviciomaquinaria.Model.DbHandler;
import com.pantaleon.serviciomaquinaria.Model.PantaleonWS;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginControler extends Functions {

    SharedPreferences sharedPreferencesLogin;
    static final String PreferencesLogin = "Login";
    Context context;
    Globals vars = Globals.getInstance();
    //TaskGetLogin taskGetLogin;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ProgressDialog pDialog;
    DbHandler dbHandler;


    public LoginControler(Context context) {
        this.context = context;
        //ws = new WebService();
        init();
    }

    private void init(){
        vars = Globals.getInstance();
        sharedPreferencesLogin = context.getSharedPreferences(PreferencesLogin, Context.MODE_PRIVATE);
        dbHandler = new DbHandler(context,null,null,1);
        createRetrofit();
    }


    public boolean existLogin() {
        Login cls = getPreferenceLogin();
        if (!cls.getUsuario().isEmpty()) {
            return true;
        }
        return false;
    }



    public String getIniciales(){
        String iniciales="";
        String[] letras = sharedPreferencesLogin.getString("usuario", "").split(" ");
        if(letras.length > 1) {
            iniciales = letras[0].substring(0, 1) + letras[1].substring(0, 1);
        }else{
            iniciales = letras[0].substring(0, 1) + letras[0].substring(1, 2);
        }
        return iniciales.toUpperCase();
    }

    public boolean loginVencido(){
        String fechaInicial = sharedPreferencesLogin.getString("fechaingreso", "");
        if(!fechaInicial.isEmpty()) {
            int resultFecha = Integer.valueOf(getDiferenciaFecha(
                    convertToDate(fechaInicial)
                    , convertToDate(getFechaActual())
                    , "D"));
            if (resultFecha <= 15) {
                return false;
            }
        }
        return true;
    }

    public Login getPreferenceLogin() {
        Login cls = new Login();
        cls.setContraseña(sharedPreferencesLogin.getString("contraseña", ""));
        cls.setUsuario(sharedPreferencesLogin.getString("usuario", ""));
        cls.setFechaIngreso(sharedPreferencesLogin.getString("fechaingreso", ""));
        return cls;
    }

    public void cerrarSesion() {
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putString("contraseña", "");
        editor.putString("usuario", "");
        editor.putString("fechaingreso", "");
        editor.commit();
        Intent intent = new Intent().setClass(context, MactyLogin.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private  void setPreferencesLogin(String Usuario,String Contraseña){
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putString("usuario",Usuario);
        editor.putString("contraseña", Contraseña);
        editor.commit();
    }

    private void setSharedPreferencesLogin(Login cls){
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit();
        editor.putString("fechaingreso", cls.getFechaIngreso());
        editor.commit();
        Intent intent = new Intent().setClass(context, MactyPrincipal.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }


    private void createRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getSeguridad() {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(vars.getWS_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PantaleonWS postService = retrofit.create(PantaleonWS.class);

        Call<LoginList> call = postService.selectSeguridad();
        call.enqueue(new Callback<LoginList>() {
            @Override
            public void onResponse(Call<LoginList> call, Response<LoginList> response) {
                LoginList models = response.body();
                if (models.getLoginList().size() > 0) {
                    dbHandler.deleteUsuario();
                    for (Login post : models.getLoginList()) {
                        dbHandler.insertUsuario(post);
                    }
                } else {
                    mensaje("No se encontro informacion para el codigo ingresado, favor de verificar", ((Activity) context)).show();
                }
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LoginList> call, Throwable t) {
                mensajeError(t.getMessage(), ((Activity)context)).show();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }

    public void getLogin(String CodigoUsuario,String Contraseña) {
        pDialog = ProgressDialog.show(context, "Consultando...", "Espere....", true, true);


        if(dbHandler.existUsuario(CodigoUsuario,Contraseña)){
            setPreferencesLogin(CodigoUsuario,Contraseña);
            Login cls = new Login();
            cls.setUsuario(CodigoUsuario);
            cls.setContraseña(Contraseña);
            cls.setFechaIngreso(getFechaHoraActual());
            setSharedPreferencesLogin(cls);
        }else{
            mensaje("Usuario o Contraseña incorrecto, favor de verificar o actualizar usuarios",((Activity)context)).show();

        }

        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }







}
