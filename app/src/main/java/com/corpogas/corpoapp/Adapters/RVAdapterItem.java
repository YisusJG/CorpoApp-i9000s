package com.corpogas.corpoapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterItem extends RecyclerView.Adapter<RVAdapterItem.RecyclerViewHeadersHolderItem> implements View.OnClickListener{

    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    View.OnClickListener listener;



    public class RecyclerViewHeadersHolderItem extends RecyclerView.ViewHolder {
        TextView txtTitulo;
        ImageView imgHeader;


        RecyclerViewHeadersHolderItem(View itemView) {
            super(itemView);
            txtTitulo = (TextView)itemView.findViewById(R.id.txtTituloItem);
            imgHeader = (ImageView)itemView.findViewById(R.id.imgItem);
        }
    }

    public  RVAdapterItem(List<RecyclerViewHeaders> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RVAdapterItem.RecyclerViewHeadersHolderItem onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        RVAdapterItem.RecyclerViewHeadersHolderItem recyclerViewHeadersHolder = new RVAdapterItem.RecyclerViewHeadersHolderItem(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapterItem.RecyclerViewHeadersHolderItem recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtTitulo.setText(lrecyclerViewHeaders.get(position).getTitulo());
        recyclerViewHeadersHolder.imgHeader.setImageResource(lrecyclerViewHeaders.get(position).getImagenId());

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
        }
    }
}
