package com.pantaleon.serviciomaquinaria.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.pantaleon.serviciomaquinaria.Adapter.ServicioAdapter;
import com.pantaleon.serviciomaquinaria.Adapter.ServicioDetalleAdapter;
import com.pantaleon.serviciomaquinaria.Class.Servicio;
import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Controler.RevisionControler;
import com.pantaleon.serviciomaquinaria.Controler.ServicioControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmDetalle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmDetalle extends Fragment implements ServicioAdapter.OnItemClickListener
        ,ServicioDetalleAdapter.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "fecha";
    private static final String ARG_PARAM3 = "transferido";

    // TODO: Rename and change types of parameters
    private String mID;
    private String mFecha;
    private int mTransferido;


    View view;
    Functions util;
    RevisionControler revisionControler;


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

    int IdServicio=0;

    public FragmDetalle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Id Local 1.
     * @param param2 Fecha 2.
     * @param param3 Transferido 3.
     * @return A new instance of fragment FragmDetalle.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmDetalle newInstance(String param1, String param2, int param3) {
        FragmDetalle fragment = new FragmDetalle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mID = getArguments().getString(ARG_PARAM1);
            mFecha = getArguments().getString(ARG_PARAM2);
            mTransferido = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragm_detalle, container, false);
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
        revisionControler = new RevisionControler(getActivity());


        arrayList = new ArrayList<>();
        revisionControler = new RevisionControler(getContext());
        controler = new ServicioControler(getContext());
        arrayDetalleList = new ArrayList<>();

        refreshLayout = (SwipeRefreshLayout) this.view.findViewById(R.id.swipeRefresh);
        grdDatos = (RecyclerView) this.view.findViewById(R.id.grdDatos);
        grdDatos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        grdDatos.setLayoutManager(llm);

        if (!mID.isEmpty()) {
            ((TextView) this.view.findViewById(R.id.lblIdEncabezado)).setText(mID);
            ((TextView) this.view.findViewById(R.id.lblFechaEncabezado)).setText(mFecha);
            buscar();
        } else {
            util.mensaje("Debe de grabar el encabezado antes del detalle, favor de verificar", getActivity()).show();
        }

    }


    private void actions() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscar();
            }
        });
    }


    // <editor-fold defaultstate="collapsed" desc="(Buscar)">
    private void buscar() {
        grdDatos.setScrollingTouchSlop(0);
        mAdapter = new ServicioAdapter(arrayList, getContext(), this);
        controler = new ServicioControler( getContext(), mAdapter, grdDatos, refreshLayout);
        controler.buscar(1, "","");
        ((TextView) this.view.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }
    // </editor-fold>


    @Override
    public void onClick(ServicioAdapter.ItemAdapterViewHolder holder, int position, int options) {
        this.IdServicio = Integer.valueOf( mAdapter.info.get(position).getIdServicio());
        dialogDetalle(mAdapter.info.get(position).getDescripcion());
    }


    // <editor-fold defaultstate="collapsed" desc="(dialog Detalle Servicios)">
    AlertDialog alertDialogDetalle;
    private void dialogDetalle(String Categoria) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getWindow().getDecorView().getRootView().getContext());
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
        int EstadoRevision = 2;
        if(mTransferido >0){
            EstadoRevision = 3;
        }
        mDetalleAdapter = new ServicioDetalleAdapter(arrayDetalleList, getContext(), this,EstadoRevision);
        controler = new ServicioControler(viewLocal.getContext(), grdDatosDetalle, refreshLayoutDetalle, mDetalleAdapter);
        controler.buscar(2,String.valueOf(IdServicio),mID);
        ((TextView) viewLocal.findViewById(R.id.lblFechaActualizacion)).setText(util.getFechaHoraActual());
    }

    @Override
    public void onClick(ServicioDetalleAdapter.ItemAdapterViewHolder holder, int position, int options) {
        util.msgToast(mDetalleAdapter.info.get(position).getDescripiconRevision(),getContext());
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



}//Fin del Fragm
