package com.corpogas.corpoapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RecyclerViewHeadersHolder> implements View.OnClickListener {

    List<RecyclerViewHeaders> lrecyclerViewHeaders;
    View.OnClickListener listener;


    public class RecyclerViewHeadersHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtTitulo;
        TextView txtSubtitulo;
        ImageView imgHeader;


        RecyclerViewHeadersHolder(View itemView) {
            super(itemView);
            cardView =(CardView)itemView.findViewById(R.id.cv);
            txtTitulo = (TextView)itemView.findViewById(R.id.txtTituloHeader);
            txtSubtitulo = (TextView)itemView.findViewById(R.id.txtSubtituloHeader);
            imgHeader = (ImageView)itemView.findViewById(R.id.imgHeader);
        }
    }

    public RVAdapter(List<RecyclerViewHeaders> recyclerViewHeaders){
        this.lrecyclerViewHeaders = recyclerViewHeaders;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerViewHeadersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headers_recycler_view, viewGroup, false);
        RecyclerViewHeadersHolder recyclerViewHeadersHolder = new RecyclerViewHeadersHolder(v);
        v.setOnClickListener(this);

        return recyclerViewHeadersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHeadersHolder recyclerViewHeadersHolder, int position) {
        recyclerViewHeadersHolder.txtTitulo.setText(lrecyclerViewHeaders.get(position).getTitulo());
        recyclerViewHeadersHolder.txtSubtitulo.setText(lrecyclerViewHeaders.get(position).getSubtitulo());
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
