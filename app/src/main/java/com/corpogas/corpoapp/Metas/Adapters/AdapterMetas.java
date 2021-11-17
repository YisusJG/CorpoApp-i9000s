package com.corpogas.corpoapp.Metas.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Cortes.CierreValePapel;

import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.Metas.Entities.Metas;
import com.corpogas.corpoapp.R;

import java.util.List;

public class AdapterMetas extends RecyclerView.Adapter<AdapterMetas.RecyclerViewHeadersHolder> implements View.OnClickListener {

    List<Metas> lrecyclerViewHeaders;
    View.OnClickListener listener;
    public boolean isClickable;
//    ImageView imgAgregarVale, imgEliminarVale;

    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {

        TextView txvTipoProducto,txvDespachos,txvMetas,txvVendidos,txvDiferencias;

        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);
            txvTipoProducto = (TextView)itemView.findViewById(R.id.txvTipoProducto);
            txvDespachos = (TextView)itemView.findViewById(R.id.txvDespachos);
            txvMetas = (TextView)itemView.findViewById(R.id.txvMetas);
            txvVendidos = (TextView)itemView.findViewById(R.id.txvVendidos);
            txvDiferencias = (TextView)itemView.findViewById(R.id.txvDiferencias);

        }
    }

    public AdapterMetas(List<Metas> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public AdapterMetas.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_metas, viewGroup, false);
        AdapterMetas.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new AdapterMetas.RecyclerViewHeadersHolder(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMetas.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txvTipoProducto.setText(String.valueOf(lrecyclerViewHeaders.get(position).getTipoProducto()));
        recyclerViewHeadersHolder.txvDespachos.setText(String.valueOf(lrecyclerViewHeaders.get(position).getDespachos()));
        recyclerViewHeadersHolder.txvMetas.setText(String.valueOf(lrecyclerViewHeaders.get(position).getMeta()));
        recyclerViewHeadersHolder.txvVendidos.setText(String.valueOf(lrecyclerViewHeaders.get(position).getVendidos()));
        recyclerViewHeadersHolder.txvDiferencias.setText(String.valueOf(lrecyclerViewHeaders.get(position).getDiferencias()));

        if(lrecyclerViewHeaders.get(position).getMeta() > lrecyclerViewHeaders.get(position).getVendidos())
        {
            recyclerViewHeadersHolder.txvDiferencias.setTextColor(Color.parseColor("#EE4B2B"));
        }else {
            recyclerViewHeadersHolder.txvDiferencias.setTextColor(Color.parseColor("#4BB543"));
        }
    }

    @Override
    public int getItemCount() {
        return lrecyclerViewHeaders.size();
    }

    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {


        if(listener!=null){
            listener.onClick(v);
            if(!isClickable)
                return;
        }

    }

}
