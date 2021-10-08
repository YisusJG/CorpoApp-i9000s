package com.corpogas.corpoapp.Login.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterPicos extends RecyclerView.Adapter<RVAdapterPicos.RecyclerViewHeadersHolder> implements View.OnClickListener{
    List<RecepcionFajilla> lrecyclerViewHeaders;
    View.OnClickListener listener;
    public boolean isClickable;

    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {

        TextView txtCantidad, txtDenominacion, txtTotal;

        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);

            txtCantidad = (TextView)itemView.findViewById(R.id.textCantidadBilletes);
            txtDenominacion = (TextView)itemView.findViewById(R.id.textDenominacionPicos);
            txtTotal = (TextView)itemView.findViewById(R.id.textTotalBilletesPicos);

        }
    }

    public RVAdapterPicos(List<RecepcionFajilla> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterPicos.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_picos, viewGroup, false);
        RVAdapterPicos.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RVAdapterPicos.RecyclerViewHeadersHolder(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterPicos.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtCantidad.setText(String.valueOf(lrecyclerViewHeaders.get(position).getCantidad()));
        recyclerViewHeadersHolder.txtDenominacion.setText(String.valueOf(lrecyclerViewHeaders.get(position).getDenominacion()));
        recyclerViewHeadersHolder.txtTotal.setText(String.valueOf(lrecyclerViewHeaders.get(position).getTotal()));

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
