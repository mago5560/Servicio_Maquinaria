package com.pantaleon.serviciomaquinaria.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.Controler.PilotoControler;
import com.pantaleon.serviciomaquinaria.Controler.RevisionControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.MactyScanner;
import com.pantaleon.serviciomaquinaria.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmEncabezado#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmEncabezado extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";

    // TODO: Rename and change types of parameters
    private String mID = "";


    View view;
    Functions util;
    PilotoControler controler;
    RevisionTecleo cls;
    RevisionControler revisionControler;
    boolean vehiculo=true;


    // <editor-fold defaultstate="collapsed" desc="(Set onClick)">
    private static OnFragmClickListener mFragmClickListener;

    public void setOnFragmClickListener(final OnFragmClickListener mItemClickListener) {
        this.mFragmClickListener = mItemClickListener;
    }

    public interface OnFragmClickListener {
        public void onClick(String ID, String Fecha);
    }
    // </editor-fold>

    public FragmEncabezado() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FragmEncabezado.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmEncabezado newInstance(String param1) {
        FragmEncabezado fragment = new FragmEncabezado();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm_encabezado, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        findViewsByIds();
        actions();
    }

    private void findViewsByIds() {
        util = new Functions();
        controler = new PilotoControler(getActivity());
        cls = new RevisionTecleo();
        revisionControler = new RevisionControler(getActivity());
        if (!mID.isEmpty()) {
            habilitaCampos(false);
            fillEncabezado();
        }

        ((TextView) view.findViewById(R.id.lblFecha)).setText(util.getFechaActual());
    }

    private void actions() {

        ((TextView) this.view.findViewById(R.id.lblFecha)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.getFechaDialog(getActivity(), ((TextView) view.findViewById(R.id.lblFecha)));
            }
        });

        ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarVehiculo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarVehiculoPiloto(true);
            }
        });

        ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarPiloto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarVehiculoPiloto(false);
            }
        });

        ((Button) this.view.findViewById(R.id.btnGrabarRevision)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarRevision();
            }
        });

        ((Button) this.view.findViewById(R.id.btnNuevaRevision)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                habilitaCampos(true);
                inicializaCampos();
            }
        });


        ((ImageButton) this.view.findViewById(R.id.imgbtnScannerVehiculo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starScanner(true);
            }
        });

        ((ImageButton) this.view.findViewById(R.id.imgbtnScannerPiloto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starScanner(false);
            }
        });
    }


    private void starScanner(boolean Vehiculo) {
        this.vehiculo = Vehiculo;
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.forSupportFragment(this)
                .setOrientationLocked(true)
                .setCaptureActivity(MactyScanner.class)
                .initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                validarBarras(result.getContents());
            } else {
                util.mensaje("No se leyo correctamente el codigo de barras, vuelva a intentar...", getActivity()).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void validarBarras(String Lectura) {
        if(vehiculo){
            ((EditText) this.view.findViewById(R.id.txtIdVehiculo)).setText(Lectura);
        }else{
            ((EditText) this.view.findViewById(R.id.txtCodPiloto)).setText(Lectura);
        }
        buscarVehiculoPiloto(vehiculo);
    }

    private void iniciarRevision() {
        if (validarCampos(false)) {
            revisionControler = new RevisionControler(getActivity());

            cls = new RevisionTecleo();
            cls.setFecha(((TextView) this.view.findViewById(R.id.lblFecha)).getText().toString());
            cls.setNombrePiloto(((TextView) this.view.findViewById(R.id.lblNombrePiloto)).getText().toString());
            cls.setEdadPiloto(((TextView) this.view.findViewById(R.id.lblEdad)).getText().toString());
            cls.setCodPiloto(((EditText) this.view.findViewById(R.id.txtCodPiloto)).getText().toString());
            cls.setLicenciaPiloto(((TextView) this.view.findViewById(R.id.lblLicencia)).getText().toString());
            cls.setFechaVenceLicencia(((TextView) this.view.findViewById(R.id.lblFechaVenceLiciencia)).getText().toString());
            cls.setIdVehiculo(((EditText) this.view.findViewById(R.id.txtIdVehiculo)).getText().toString());
            cls.setPlaca(((TextView) this.view.findViewById(R.id.lblPlaca)).getText().toString());
            cls.setTC(((EditText) this.view.findViewById(R.id.txtTC)).getText().toString());
            if (!cls.getNombrePiloto().equals("---")) {
                mID = String.valueOf(revisionControler.insertEncabezado(cls));
                habilitaCampos(false);
                mFragmClickListener.onClick(mID, cls.getFecha());
            } else {
                util.mensaje("Debe de realizar la busqueda del vehiculo antes de inicializar la revision", getActivity()).show();
            }
        }
    }

    private void buscarVehiculoPiloto(boolean Vehiculo) {
        if (validarCampos(Vehiculo)) {

            controler = new PilotoControler(getActivity(),
                    ((TextView) this.view.findViewById(R.id.lblNombrePiloto)),
                    ((TextView) this.view.findViewById(R.id.lblLicencia)),
                    ((TextView) this.view.findViewById(R.id.lblEdad)),
                    ((TextView) this.view.findViewById(R.id.lblFechaVenceLiciencia)),
                    ((TextView) this.view.findViewById(R.id.lblPlaca)),
                    ((EditText) this.view.findViewById(R.id.txtCodPiloto)),
                    ((EditText) this.view.findViewById(R.id.txtTC)));

            if (Vehiculo) {
                controler.buscarVehiculoPiloto(true, ((EditText) this.view.findViewById(R.id.txtIdVehiculo)).getText().toString());
            } else {
                controler.buscarVehiculoPiloto(false,((EditText) this.view.findViewById(R.id.txtCodPiloto)).getText().toString());
            }
            /*
            //Cosulta en linea, se reemplaza por consulta local
            if (Vehiculo) {
                controler.getVehiculo(Integer.valueOf(((EditText) this.view.findViewById(R.id.txtIdVehiculo)).getText().toString()));
            } else {
                controler.getPiloto(Integer.valueOf(((EditText) this.view.findViewById(R.id.txtCodPiloto)).getText().toString()));
            }
             */
        }
    }


    private boolean validarCampos(boolean Vehiculo) {
        if (Vehiculo) {
            if (util.validarCampoVacio(((EditText) this.view.findViewById(R.id.txtIdVehiculo)))) {
                return false;
            }
        } else {
            if (util.validarCampoVacio(((EditText) this.view.findViewById(R.id.txtIdVehiculo)))) {
                return false;
            }
            if (util.validarCampoVacio(((EditText) this.view.findViewById(R.id.txtCodPiloto)))) {
                return false;
            }
        }
        return true;
    }


    private void habilitaCampos(boolean Nuevo) {
        if (Nuevo) {
            ((EditText) this.view.findViewById(R.id.txtIdVehiculo)).setEnabled(true);
            ((EditText) this.view.findViewById(R.id.txtCodPiloto)).setEnabled(true);
            ((EditText) this.view.findViewById(R.id.txtTC)).setEnabled(true);
            ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarVehiculo)).setVisibility(View.VISIBLE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarPiloto)).setVisibility(View.VISIBLE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnScannerPiloto)).setVisibility(View.VISIBLE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnScannerVehiculo)).setVisibility(View.VISIBLE);
            ((Button) this.view.findViewById(R.id.btnGrabarRevision)).setVisibility(View.VISIBLE);
            ((TextView) this.view.findViewById(R.id.lblFecha)).setEnabled(true);
            ((Button) this.view.findViewById(R.id.btnNuevaRevision)).setVisibility(View.GONE);
        } else {
            ((EditText) this.view.findViewById(R.id.txtIdVehiculo)).setEnabled(false);
            ((EditText) this.view.findViewById(R.id.txtCodPiloto)).setEnabled(false);
            ((EditText) this.view.findViewById(R.id.txtTC)).setEnabled(false);
            ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarVehiculo)).setVisibility(View.GONE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnBuscarPiloto)).setVisibility(View.GONE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnScannerPiloto)).setVisibility(View.GONE);
            ((ImageButton) this.view.findViewById(R.id.imgbtnScannerVehiculo)).setVisibility(View.GONE);
            ((Button) this.view.findViewById(R.id.btnGrabarRevision)).setVisibility(View.GONE);
            ((TextView) this.view.findViewById(R.id.lblFecha)).setEnabled(false);
            ((Button) this.view.findViewById(R.id.btnNuevaRevision)).setVisibility(View.VISIBLE);
        }

    }

    private void fillEncabezado() {


        revisionControler = new RevisionControler(getActivity(),
                ((TextView) this.view.findViewById(R.id.lblNombrePiloto)),
                ((TextView) this.view.findViewById(R.id.lblLicencia)),
                ((TextView) this.view.findViewById(R.id.lblEdad)),
                ((TextView) this.view.findViewById(R.id.lblFechaVenceLiciencia)),
                ((TextView) this.view.findViewById(R.id.lblPlaca)),
                ((EditText) this.view.findViewById(R.id.txtCodPiloto)),
                ((EditText) this.view.findViewById(R.id.txtTC)),
                ((TextView) this.view.findViewById(R.id.lblFecha)),
                ((EditText) this.view.findViewById(R.id.txtIdVehiculo))
        );
        revisionControler.buscar(1, mID);

    }


    private void inicializaCampos() {
        ((TextView) this.view.findViewById(R.id.lblNombrePiloto)).setText("---");
        ((TextView) this.view.findViewById(R.id.lblEdad)).setText("---");
        ((EditText) this.view.findViewById(R.id.txtCodPiloto)).setText("");
        ((TextView) this.view.findViewById(R.id.lblLicencia)).setText("---");
        ((TextView) this.view.findViewById(R.id.lblFechaVenceLiciencia)).setText("---");
        ((TextView) this.view.findViewById(R.id.lblFecha)).setText(util.getFechaActual());
        ((EditText) this.view.findViewById(R.id.txtIdVehiculo)).setText("");
        mID = "";
        mFragmClickListener.onClick("", "");
    }

}//Fin Fragm
