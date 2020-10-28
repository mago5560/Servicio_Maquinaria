package com.pantaleon.serviciomaquinaria.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pantaleon.serviciomaquinaria.Class.ServicioDetalle;
import com.pantaleon.serviciomaquinaria.Controler.RevisionControler;
import com.pantaleon.serviciomaquinaria.Customs.Functions;
import com.pantaleon.serviciomaquinaria.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServicioDetalleAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public List<ServicioDetalle> info;
    List<ServicioDetalle> aux;
    private final Context context;
    private static OnItemClickListener mItemClickListener;
    private RevisionControler revisionControler;

    private int estadoBusqueda= 0;
    Functions util = new Functions();
    double ponderasion=0;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position, int options);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    public ServicioDetalleAdapter(List<ServicioDetalle> info, Context context, OnItemClickListener mItemClickListener,int EstadoBusqueda) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;
        this.estadoBusqueda = EstadoBusqueda;
        revisionControler = new RevisionControler(context);

    }

    public void setInfo(List<ServicioDetalle> info) {
        this.info = info;
        this.aux = new ArrayList<>(info);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            //Inflating header view
            View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_grd_datos, parent, false);
            return new HeaderViewHolder(tarjeta);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_grd_datos, parent, false);
            return new FooterViewHolder(tarjeta);
        }
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_servicio_detalle, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Personas");

        } else if (holder instanceof ItemAdapterViewHolder) {
            final ServicioDetalle get = info.get(position);
            final ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;
            holderItem.lblCVDescripcion.setText(get.getDescripiconRevision());
            holderItem.lblCVPunteoSistema.setText(get.getPunteoSistema());
            holderItem.txtCVPunteoReal.setText(get.getPunteoReal());
            holderItem.lblCVPonderacion.setText(get.getPonderacion());
            holderItem.txtCVObservaciones.setText(get.getObservaciones());

            switch (estadoBusqueda){
                case 2: //Grabar punteo
                    holderItem.llyCVTecleo.setVisibility(View.VISIBLE);
                    break;
                case 3://Visualizar Registro
                    holderItem.llyCVTecleo.setVisibility(View.VISIBLE);
                    holderItem.txtCVPunteoReal.setEnabled(false);
                    holderItem.txtCVObservaciones.setEnabled(false);
                    break;
                    default: //Consulta de Servicio Detalle
                        holderItem.llyCVTecleo.setVisibility(View.GONE);
                        break;
            }

            //This will randomly color all circle
            Random mRandom = new Random();
            int color =  Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            holderItem.imgvwCVColor.setBackgroundColor(color);

           /*
            holderItem.txtCVPunteoReal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(i == EditorInfo.IME_ACTION_SEND){
                        if(holderItem.txtCVPunteoReal.getText().toString().isEmpty()){
                            holderItem.txtCVPunteoReal.setText("0");
                        }
                        double punteoReal = Double.valueOf(textView.getText().toString());
                        double punteoSistema = Double.valueOf(holderItem.lblCVPunteoSistema.getText().toString());
                        ponderasion = (int) (punteoReal * 100) / punteoSistema;
                        holderItem.lblCVPonderacion.setText(String.valueOf((int)ponderasion));
                        get.setPunteoReal(textView.getText().toString());
                        get.setObservaciones(holderItem.txtCVObservaciones.getText().toString());
                        get.setPonderacion(String.valueOf(ponderasion));
                        setPonderasion(get);
                        holderItem.txtCVObservaciones.requestFocus();
                    }
                    return true;
                }
            });
            */

            holderItem.txtCVPunteoReal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(!b){
                        if(holderItem.txtCVPunteoReal.getText().toString().isEmpty()){
                            holderItem.txtCVPunteoReal.setText("0");
                        }
                        double punteoReal = Double.valueOf(holderItem.txtCVPunteoReal.getText().toString());
                        double punteoSistema = Double.valueOf(holderItem.lblCVPunteoSistema.getText().toString());
                        ponderasion = (int) (punteoReal * 100) / punteoSistema;
                        holderItem.lblCVPonderacion.setText(String.valueOf((int)ponderasion));
                        get.setPunteoReal(holderItem.txtCVPunteoReal.getText().toString());
                        get.setObservaciones(holderItem.txtCVObservaciones.getText().toString());
                        get.setPonderacion(String.valueOf(ponderasion));
                        setPonderasion(get);
                        holderItem.txtCVObservaciones.requestFocus();
                    }
                }
            });

            /*
            holderItem.txtCVObservaciones.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(i == EditorInfo.IME_ACTION_SEND){
                        if(holderItem.txtCVPunteoReal.getText().toString().isEmpty()){
                            holderItem.txtCVPunteoReal.setText("0");
                        }
                        double punteoReal = Double.valueOf(holderItem.txtCVPunteoReal.getText().toString());
                        double punteoSistema = Double.valueOf(holderItem.lblCVPunteoSistema.getText().toString());
                        ponderasion = (int) (punteoReal * 100) / punteoSistema;
                        holderItem.lblCVPonderacion.setText(String.valueOf((int)ponderasion));
                        get.setPunteoReal(holderItem.txtCVPunteoReal.getText().toString());
                        get.setObservaciones(textView.getText().toString());
                        get.setPonderacion(String.valueOf(ponderasion));
                        setPonderasion(get);
                    }
                    return true;
                }
            });
             */

            holderItem.txtCVObservaciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(!b){
                        if(holderItem.txtCVPunteoReal.getText().toString().isEmpty()){
                            holderItem.txtCVPunteoReal.setText("0");
                        }
                        double punteoReal = Double.valueOf(holderItem.txtCVPunteoReal.getText().toString());
                        double punteoSistema = Double.valueOf(holderItem.lblCVPunteoSistema.getText().toString());
                        ponderasion = (int) (punteoReal * 100) / punteoSistema;
                        holderItem.lblCVPonderacion.setText(String.valueOf((int)ponderasion));
                        get.setPunteoReal(holderItem.txtCVPunteoReal.getText().toString());
                        get.setObservaciones(holderItem.txtCVObservaciones.getText().toString());
                        get.setPonderacion(String.valueOf(ponderasion));
                        setPonderasion(get);
                    }
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("No se encontraron mas registros...");
        }
    }


    private void setPonderasion(ServicioDetalle cls){
       cls.setIdRevisionDetalle(revisionControler.insertupdateDetalle(cls));
    }

    @Override
    public int getItemCount() {
        if (info.size() == 0) {
            return 0;
        } else
            return info.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (position == 0) {
            //Se quita el Header devido a que en la lista quita el ultimo registro o en este caso el registro en la posicion 0 30/01/2019
            //return TYPE_HEADER;
        } else if (position == info.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lblCVDescripcion,lblCVPunteoSistema,lblCVPonderacion;
        private EditText txtCVPunteoReal,txtCVObservaciones;
        private LinearLayout llyCVTecleo;
        private CardView cv;
        private ImageView imgvwCVColor;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);

            lblCVDescripcion = (TextView) itemView.findViewById(R.id.lblCVDescripcion);
            lblCVPunteoSistema = (TextView) itemView.findViewById(R.id.lblCVPunteoSistema);
            lblCVPonderacion = (TextView) itemView.findViewById(R.id.lblCVPonderacion);
            imgvwCVColor = (ImageView) itemView.findViewById(R.id.imgvwCVColor);
            txtCVPunteoReal = (EditText) itemView.findViewById(R.id.txtCVPunteoReal);
            txtCVObservaciones = (EditText) itemView.findViewById(R.id.txtCVObservaciones);
            llyCVTecleo = (LinearLayout) itemView.findViewById(R.id.llyCVTecleo);

            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition(), 1);
                }
            }
        }

    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.footer_text);
        }
    }

    @Override
    public Filter getFilter() {
        return getBuscar;
    }

    private Filter getBuscar = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ServicioDetalle> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(aux);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ServicioDetalle item : aux) {
                    if (item.toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            info.clear();
            info.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
