package com.corpogas.corpoapp.Entregas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Cortes.CierreValePapel;
import com.corpogas.corpoapp.R;

import java.util.List;


public class RVAdapterValesPapel extends RecyclerView.Adapter<RVAdapterValesPapel.RecyclerViewHeadersHolder> implements View.OnClickListener {

    List<CierreValePapel> lrecyclerViewHeaders;
    View.OnClickListener listener;
    public boolean isClickable;
//    ImageView imgAgregarVale, imgEliminarVale;

    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtCantidad,txtDenominacion,txtTotal;






        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);
            cardView =(CardView)itemView.findViewById(R.id.cv);
            txtCantidad = (TextView)itemView.findViewById(R.id.txtCantidadVales);
            txtDenominacion = (TextView)itemView.findViewById(R.id.txtDenominacionVales);
            txtTotal = (TextView)itemView.findViewById(R.id.txtTotalVales);
//            imgAgregarVale = (ImageView)itemView.findViewById(R.id.imgAgregarCantidadVale);
//            imgEditarVale = (ImageView)itemView.findViewById(R.id.imgEditarCantidadVale);
//            imgEliminarVale = (ImageView)itemView.findViewById(R.id.imgEliminarCantidadVale);
//            imgDetalleVale = (ImageView)itemView.findViewById(R.id.imgDetalleVale);
        }
    }



    public RVAdapterValesPapel(List<CierreValePapel> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headers_recycler_vales, viewGroup, false);
        RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RecyclerViewHeadersHolder(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtCantidad.setText(String.valueOf(lrecyclerViewHeaders.get(position).getCantidad()));
        recyclerViewHeadersHolder.txtDenominacion.setText(String.valueOf(lrecyclerViewHeaders.get(position).getDenominacion()));
        recyclerViewHeadersHolder.txtTotal.setText(String.valueOf(lrecyclerViewHeaders.get(position).getImporte()));
//        recyclerViewHeadersHolder.imgAgregarVale.setImageResource(lrecyclerViewHeaders.get(position).getImgAgregarVale());
//        recyclerViewHeadersHolder.imgEditarVale.setImageResource(lrecyclerViewHeaders.get(position).getImgEditarVale());
//        recyclerViewHeadersHolder.imgEliminarVale.setImageResource(lrecyclerViewHeaders.get(position).getImgEliminarVale());
//        recyclerViewHeadersHolder.imgDetalleVale.setImageResource(lrecyclerViewHeaders.get(position).getImgDetalleVale());


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
