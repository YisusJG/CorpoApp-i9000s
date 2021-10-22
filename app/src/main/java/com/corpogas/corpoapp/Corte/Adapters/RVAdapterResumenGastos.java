package com.corpogas.corpoapp.Corte.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Virtuales.FormaPagoTotal;
import com.corpogas.corpoapp.Entities.Virtuales.GastoTotal;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterResumenGastos extends RecyclerView.Adapter<RVAdapterResumenGastos.RecyclerViewHeadersHolder> {

    List<GastoTotal> lrecyclerViewHeaders;
    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {

        TextView txtConceptoGasto,txtImporte;
        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);

            txtConceptoGasto = (TextView)itemView.findViewById(R.id.txtResumenGasto);
            txtImporte = (TextView)itemView.findViewById(R.id.txtResumenImporte);

        }
    }



    public RVAdapterResumenGastos(List<GastoTotal> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterResumenGastos.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_resumen_gastos, viewGroup, false);
        RVAdapterResumenGastos.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RVAdapterResumenGastos.RecyclerViewHeadersHolder(v);


        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterResumenGastos.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtConceptoGasto.setText(String.valueOf(lrecyclerViewHeaders.get(position).getConceptoGastoId()));
        recyclerViewHeadersHolder.txtImporte.setText(String.valueOf(lrecyclerViewHeaders.get(position).getImporte()));

    }

    @Override
    public int getItemCount() {
        return lrecyclerViewHeaders.size();
    }
}
