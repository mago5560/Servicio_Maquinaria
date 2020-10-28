package com.pantaleon.serviciomaquinaria.Customs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.pantaleon.serviciomaquinaria.BuildConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Functions {

    Globals vars = Globals.getInstance();

    public String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }



    public String getFechaHoraActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }


    public String formatDateUSA(String DateString){
        SimpleDateFormat simpleDateFormat = new   SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.US);
        SimpleDateFormat sdf = new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        //Long date = Date.parse(DateString);
        //String FechaHora = sdf.format(date);
        String FechaHora =  parseDate(DateString,simpleDateFormat,sdf);
        return FechaHora;
    }


    private   String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDateString;
    }




    public String getVersion( Context context) {
        int currentVersionCode= 0;
        String currentVersionName="";
        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }
        return "Version: " + Integer.toString(currentVersionCode) + "." + currentVersionName;
    }

    public AlertDialog mensaje(String Mensaje, Activity Macty ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje del Sistema");
        builder.setMessage(Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public AlertDialog mensajeError(String Mensaje, Activity Macty ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Macty);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle("Mensaje de Error");
        builder.setMessage("Se detecto un error, favor de reporta a TI.\nError referencia " + Mensaje);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public void msgSnackBar(String mensaje, Context context){
        Snackbar.make(((Activity)context).getWindow().getDecorView().getRootView(),mensaje, Snackbar.LENGTH_LONG)
                .show();
    }


    public void msgToast(String Mensaje,Context context){
        Toast.makeText(context,Mensaje,Toast.LENGTH_LONG).show();
    }

    public  String getPath(){
        String picturePath="";

        File file = new File(Environment.getExternalStorageDirectory(), "DCIM" + vars.getPATH_FILE_API());
        boolean isDirectoryCreated = file.exists();
        if(!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }
        if(isDirectoryCreated){
            picturePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM" + vars.getPATH_FILE_API() + File.separator;
        }


        return picturePath;
    }


    public boolean validarCampoVacio(EditText editText){
        String Cadena = editText.getText().toString();
        if(TextUtils.isEmpty(Cadena)){
            editText.setError("Campo requerido");
            editText.requestFocus();
            return true;
        }else{
            editText.setError(null);
        }
        return false;
    }




    public String getDiferenciaFecha(Date fechaInicial, Date fechaFinal,String DatePart){

        String Resultado = "";
        double calculoDif;
        int calucloDias;
        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();

        switch (DatePart) {
            case  "D":
                calculoDif =  (diferencia / 86400000);
                Resultado = String.valueOf((int) calculoDif);
                break;
            case "H":
                calculoDif = Double.valueOf(diferencia) / 3600000;
                Resultado = String.valueOf(  Double.parseDouble(new DecimalFormat("####.####").format(calculoDif)));
                break;
        }
        return  Resultado;
    }

    public Date convertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }




}
