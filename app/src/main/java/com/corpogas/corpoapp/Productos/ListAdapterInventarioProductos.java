package com.corpogas.corpoapp.Productos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.corpogas.corpoapp.R;

import java.util.List;

public class ListAdapterInventarioProductos extends ArrayAdapter<String> implements Filterable {
    private Activity context;
    private String[] Existencias;
    private String[] DescripcionProducto;
    private String[] ClaveProducto;

    public ListAdapterInventarioProductos(Activity context, List<String> Existencias, List<String> DescripcionProducto, List<String> ClaveProducto) {
        super(context, R.layout.listainventarioaceites, Existencias);
        this.context=(Activity) context;
        this.Existencias = Existencias.toArray(new String[0]);
        this.DescripcionProducto = DescripcionProducto.toArray(new String[0]);
        this.ClaveProducto = ClaveProducto.toArray(new String[0]);

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.listainventarioaceites, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.tvexistencias);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView identificadorProducto = (TextView) rowView.findViewById(R.id.identificadorProducto);

        titleText.setText(Existencias[position]);
        subtitleText.setText(DescripcionProducto[position]);
        identificadorProducto.setText(ClaveProducto[position]);

        return rowView;

    }
}
