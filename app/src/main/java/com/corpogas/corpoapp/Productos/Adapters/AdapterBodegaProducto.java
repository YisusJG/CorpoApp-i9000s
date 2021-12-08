package com.corpogas.corpoapp.Productos.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.R;
import java.util.List;

public class AdapterBodegaProducto extends RecyclerView.Adapter<AdapterBodegaProducto.RecyclerViewHeadersHolder> implements View.OnClickListener {

    List<BodegaProducto> lRecyclerViewHeaders;
    View.OnClickListener listener;
    public boolean isClickable;

    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder{

        TextView txtNombreProductoInventario, txtNumeroId, txtNumeroExistencias;

        RecyclerViewHeadersHolder(View itemView){
            super((itemView));
            txtNombreProductoInventario = (TextView) itemView.findViewById(R.id.txtNombreProductoInventario);
            txtNumeroId = (TextView) itemView.findViewById(R.id.txtNumeroId);
            txtNumeroExistencias = (TextView)  itemView.findViewById(R.id.txtNumeroExistencias);

        }

    }

    public AdapterBodegaProducto(List<BodegaProducto> recyclerViewHeaders){
        this.lRecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @NonNull
    @Override
    public AdapterBodegaProducto.RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_inventario, viewGroup, false);
        AdapterBodegaProducto.RecyclerViewHeadersHolder recyclerViewHeadersHolder = new AdapterBodegaProducto.RecyclerViewHeadersHolder(v);
        return  recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBodegaProducto.RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position){
        recyclerViewHeadersHolder.txtNombreProductoInventario.setText(String.valueOf(lRecyclerViewHeaders.get(position).Producto.getLongDescription()));
        recyclerViewHeadersHolder.txtNumeroId.setText(String.valueOf(lRecyclerViewHeaders.get(position).getProductoId()));
        recyclerViewHeadersHolder.txtNumeroExistencias.setText(String.valueOf(lRecyclerViewHeaders.get(position).getExistencias()));
    }

    @Override
    public int getItemCount(){return lRecyclerViewHeaders.size(); }

    public void setOnClickListener(View.OnClickListener listener){ this.listener = listener;}

    @Override
    public void onClick(View v){
        if(listener!=null){
            listener.onClick(v);
            if(!isClickable)
                return;
        }
    }
}
