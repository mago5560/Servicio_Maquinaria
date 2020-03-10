package com.pantaleon.serviciomaquinaria.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pantaleon.serviciomaquinaria.Class.RevisionTecleo;
import com.pantaleon.serviciomaquinaria.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RevisionTecleoAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public List<RevisionTecleo> info;
    List<RevisionTecleo> aux;
    private final Context context;
    private static OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        public void onClick(ItemAdapterViewHolder holder, int position, int options);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    public RevisionTecleoAdapter(List<RevisionTecleo> info, Context context, OnItemClickListener mItemClickListener) {
        this.info = info;
        this.aux = new ArrayList<>(info);
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public void setInfo(List<RevisionTecleo> info) {
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
        View tarjeta = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_revision_local, parent, false);
        return new ItemAdapterViewHolder(tarjeta);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Personas");

        } else if (holder instanceof ItemAdapterViewHolder) {
            final RevisionTecleo get = info.get(position);
            ItemAdapterViewHolder holderItem = (ItemAdapterViewHolder) holder;

            if(get.getTransferido() >0){
                holderItem.lblCVTransferido.setText("Transferido");
                holderItem.btnTransferir.setVisibility(View.GONE);
                holderItem.btnEliminar.setVisibility(View.GONE);
            }else{
                holderItem.lblCVTransferido.setText("Pendiente de transferir");
                holderItem.btnTransferir.setVisibility(View.VISIBLE);
                holderItem.btnEliminar.setVisibility(View.VISIBLE);
            }

            holderItem.lblCVId.setText(String.valueOf(get.getId()));
            holderItem.lblCVIdVehiculo.setText(get.getIdVehiculo());
            holderItem.lblCVNombrePiloto.setText(get.getNombrePiloto());
            holderItem.lblCVFecha.setText(get.getFecha());
            holderItem.lblCVIdentificador.setText(get.getIdentificadorDB());
            //This will randomly color all circle
            Random mRandom = new Random();
            int color =  Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
            holderItem.llyCorrelativo.setBackgroundColor(color);


        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("No se encontraron mas registros...");
        }
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
        private TextView lblCVId, lblCVNombrePiloto, lblCVIdVehiculo, lblCVTransferido, lblCVFecha,lblCVIdentificador;
        private CardView cv;
        private LinearLayout llyCorrelativo;
        private Button btnTransferir,btnEliminar;

        public ItemAdapterViewHolder(View itemView) {
            super(itemView);

            lblCVTransferido = (TextView) itemView.findViewById(R.id.lblCVTransferido);
            lblCVId = (TextView) itemView.findViewById(R.id.lblCVId);
            lblCVNombrePiloto = (TextView) itemView.findViewById(R.id.lblCVNombrePiloto);
            lblCVIdVehiculo = (TextView) itemView.findViewById(R.id.lblCVIdVehiculo);
            lblCVFecha = (TextView) itemView.findViewById(R.id.lblCVFecha);
            lblCVIdentificador = (TextView) itemView.findViewById(R.id.lblCVIdentificadorDB);
            llyCorrelativo =(LinearLayout) itemView.findViewById(R.id.llyCorrelativo);

            btnTransferir = (Button) itemView.findViewById(R.id.btnTransferir);
            btnTransferir.setOnClickListener(this);
            btnEliminar = (Button) itemView.findViewById(R.id.btnEliminar);
            btnEliminar.setOnClickListener(this);

            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                if (v == cv) {
                    mItemClickListener.onClick(this, getLayoutPosition(), 1);
                }else if (v == btnTransferir) {
                    mItemClickListener.onClick(this, getLayoutPosition(), 2);
                }else if (v == btnEliminar) {
                    mItemClickListener.onClick(this, getLayoutPosition(), 3);
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
            List<RevisionTecleo> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(aux);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (RevisionTecleo item : aux) {
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
