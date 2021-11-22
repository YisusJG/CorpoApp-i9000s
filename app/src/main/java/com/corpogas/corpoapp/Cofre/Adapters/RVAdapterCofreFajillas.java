package com.corpogas.corpoapp.Cofre.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Cofre.Entities.TotalFajillaCajaFuerte;
import com.corpogas.corpoapp.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class RVAdapterCofreFajillas extends RecyclerView.Adapter<RVAdapterCofreFajillas.RecyclerViewHeadersHolder> implements View.OnClickListener {

    List<TotalFajillaCajaFuerte> lrecyclerViewHeaders;
    View.OnClickListener listener;
    public boolean isClickable;
    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    DecimalFormat df;

//    ImageView imgAgregarVale, imgEliminarVale;

    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {       
        TextView txtCantidadCofre,txtPrecioCofre,txtImporteCofre;


        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);

            txtCantidadCofre = (TextView)itemView.findViewById(R.id.txtCantidadCofre);
            txtPrecioCofre = (TextView)itemView.findViewById(R.id.txtPrecioCofre);
            txtImporteCofre = (TextView)itemView.findViewById(R.id.txtImporteCofre);
        }
    }



    public RVAdapterCofreFajillas(List<TotalFajillaCajaFuerte> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterCofreFajillas.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cofre, viewGroup, false);
        RVAdapterCofreFajillas.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RVAdapterCofreFajillas.RecyclerViewHeadersHolder(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterCofreFajillas.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("$#,###.00##",simbolos);
        df.setMaximumFractionDigits(2);

        recyclerViewHeadersHolder.txtCantidadCofre.setText(String.valueOf(lrecyclerViewHeaders.get(position).Cantidad));
        recyclerViewHeadersHolder.txtPrecioCofre.setText(String.valueOf(lrecyclerViewHeaders.get(position).Folio));
        recyclerViewHeadersHolder.txtImporteCofre.setText(df.format(Double.parseDouble(String.valueOf(lrecyclerViewHeaders.get(position).ImporteTotal))));

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
