package com.corpogas.corpoapp.Corte.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Virtuales.FormaPagoTotal;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterResumenFormaPagos extends RecyclerView.Adapter<RVAdapterResumenFormaPagos.RecyclerViewHeadersHolder>{

    List<FormaPagoTotal> lrecyclerViewHeaders;
    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {

        TextView txtFormaPago,txtImporte;
        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);

            txtFormaPago = (TextView)itemView.findViewById(R.id.txtRescumenFormaPago);
            txtImporte = (TextView)itemView.findViewById(R.id.txtResumenImporte);

        }
    }



    public RVAdapterResumenFormaPagos(List<FormaPagoTotal> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterResumenFormaPagos.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_resumen_formas_pago, viewGroup, false);
        RVAdapterResumenFormaPagos.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RVAdapterResumenFormaPagos.RecyclerViewHeadersHolder(v);


        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterResumenFormaPagos.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtFormaPago.setText(String.valueOf(lrecyclerViewHeaders.get(position).getFormaPago()));
        recyclerViewHeadersHolder.txtImporte.setText(String.valueOf(lrecyclerViewHeaders.get(position).getImporte()));

    }

    @Override
    public int getItemCount() {
        return lrecyclerViewHeaders.size();
    }
}
