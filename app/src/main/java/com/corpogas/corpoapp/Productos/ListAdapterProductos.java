package com.corpogas.corpoapp.Productos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;


import com.corpogas.corpoapp.R;

import java.util.List;

public class ListAdapterProductos extends ArrayAdapter<String> implements Filterable{
    private final Activity context;
    private final String[] ID;
    private final String[] DescripcionProducto;

     public ListAdapterProductos(Activity context, List<String> ID, List<String> DescripcionProducto){
        super((Context) context, R.layout.listaproductos, ID);
        //            ,
        this.context=(Activity) context;
        this.ID = ID.toArray(new String[0]);
        this.DescripcionProducto = DescripcionProducto.toArray(new String[0]);
    }




    public View getView(int position, View view, ViewGroup parent) {
        //LayoutInflater inflater=context.getLayoutInflater();
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.listaproductos, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(ID[position]);
        subtitleText.setText(DescripcionProducto[position]);
        return rowView;
    };
}
