package com.corpogas.corpoapp.Corte.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Corte.Entities.ValePapelTotal;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterResumenVales extends RecyclerView.Adapter<RVAdapterResumenVales.RecyclerViewHeadersHolder>{

    List<ValePapelTotal> lrecyclerViewHeaders;
    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {

        TextView txtCantidad,txtDenominacion,txtTotal;
        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);

            txtCantidad = (TextView)itemView.findViewById(R.id.txtRescumenCantidad);
            txtDenominacion = (TextView)itemView.findViewById(R.id.txtResumenDenominacion);
            txtTotal = (TextView)itemView.findViewById(R.id.txtResumenTotal);

        }
    }

    public RVAdapterResumenVales(List<ValePapelTotal> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterResumenVales.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_resumen_vales_total, viewGroup, false);
        RVAdapterResumenVales.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RVAdapterResumenVales.RecyclerViewHeadersHolder(v);


        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterResumenVales.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtCantidad.setText(String.valueOf(lrecyclerViewHeaders.get(position).getCantidad()));
        recyclerViewHeadersHolder.txtDenominacion.setText(String.valueOf(lrecyclerViewHeaders.get(position).getDenominacion()));
        recyclerViewHeadersHolder.txtTotal.setText(String.valueOf(lrecyclerViewHeaders.get(position).getTotal()));
    }

    @Override
    public int getItemCount() {
        return lrecyclerViewHeaders.size();
    }
}
