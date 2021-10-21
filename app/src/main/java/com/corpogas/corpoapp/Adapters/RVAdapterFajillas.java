package com.corpogas.corpoapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Fajillas.RecyclerViewHeadersFajillas;
import com.corpogas.corpoapp.R;

import java.util.List;

public class RVAdapterFajillas extends RecyclerView.Adapter<RVAdapterFajillas.RecyclerViewHeadersFajillasHolder> implements  View.OnClickListener{

        List<RecyclerViewHeadersFajillas> lrecyclerHeadersFajillas;
        View.OnClickListener listener;

        public class RecyclerViewHeadersFajillasHolder extends RecyclerView.ViewHolder {
                CardView cardView;
                TextView txtTipoFajilla, txtTotalFajillas, txtTotalMontoFajillas, txtRecibioFajillas, txtEstatus;
                ImageView imgHeader;

                public RecyclerViewHeadersFajillasHolder(@NonNull View itemView) {
                        super(itemView);
                        cardView = (CardView)itemView.findViewById(R.id.cvFajillas);
                        txtTipoFajilla = (TextView)itemView.findViewById(R.id.txtTipoFajilla);
                        txtTotalFajillas = (TextView)itemView.findViewById(R.id.txtToalFajillas);
                        txtTotalMontoFajillas = (TextView)itemView.findViewById(R.id.txtTotalMontoFajillas);
                        txtRecibioFajillas = (TextView)itemView.findViewById(R.id.txtRecibioFajillas);
                        imgHeader = (ImageView)itemView.findViewById(R.id.imgHeader);
                        txtEstatus = (TextView)itemView.findViewById(R.id.txtEstatus);
                }
        }

        public RVAdapterFajillas(List<RecyclerViewHeadersFajillas> recyclerViewHeadersFajillas){
                this.lrecyclerHeadersFajillas = recyclerViewHeadersFajillas;
        }


        public void onAttachedToRecycler(@NonNull RecyclerView recyclerView) {
                super.onAttachedToRecyclerView(recyclerView);
        }

        @NonNull
        @Override
        public RVAdapterFajillas.RecyclerViewHeadersFajillasHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headers_recycler_view_fajillas, viewGroup, false);
                RecyclerViewHeadersFajillasHolder recyclerViewHeadersFajillasHolder = new RecyclerViewHeadersFajillasHolder(v);
                v.setOnClickListener(this);
                return recyclerViewHeadersFajillasHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHeadersFajillasHolder recyclerViewHeadersFajillasHolder, int position) {
                recyclerViewHeadersFajillasHolder.txtTipoFajilla.setText(lrecyclerHeadersFajillas.get(position).getTipoFajilla());
                recyclerViewHeadersFajillasHolder.txtTotalFajillas.setText(lrecyclerHeadersFajillas.get(position).getCantidadFajillas());
                recyclerViewHeadersFajillasHolder.txtTotalMontoFajillas.setText(lrecyclerHeadersFajillas.get(position).getMontoFajillas());
                recyclerViewHeadersFajillasHolder.txtRecibioFajillas.setText(lrecyclerHeadersFajillas.get(position).getAutorizo());
                recyclerViewHeadersFajillasHolder.txtEstatus.setText(lrecyclerHeadersFajillas.get(position).getEntradasalida());
//        recyclerViewHeadersFajillasHolder.imgHeader.setImageResource(lrecyclerHeadersFajillas.get(position).getImage);

        }

        @Override
        public int getItemCount() {
                return lrecyclerHeadersFajillas.size();
        }

        public void setOnClickListener(View.OnClickListener listener)
        {
                this.listener = listener;
        }

        @Override
        public void onClick(View v) {
                if (listener!= null){
                        listener.onClick(v);
                }
        }

}

