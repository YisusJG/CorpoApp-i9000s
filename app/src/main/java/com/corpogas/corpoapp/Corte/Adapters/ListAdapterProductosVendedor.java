package com.corpogas.corpoapp.Corte.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.corpogas.corpoapp.R;

import java.util.List;

public class ListAdapterProductosVendedor extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final String[] calculo;

    public ListAdapterProductosVendedor(Activity context, List<String> maintitle, List<String> subtitle, List<String> total1) {
        super(context, R.layout.list, maintitle);

        this.context = context;
        this.maintitle = maintitle.toArray(new String[0]);
        this.subtitle = subtitle.toArray(new String[0]);
        this.calculo = total1.toArray(new String[0]);
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_cierre_formas_pago_vendedor, null, true);
        rowView.setClickable(false);
        rowView.setEnabled(false);
        rowView.setActivated(false);
        rowView.setFocusable(false);

        TextView cantidad = rowView.findViewById(R.id.txtNumeroFormasPagoVendedor);
        TextView formasPago = rowView.findViewById(R.id.txtFormasPagoVendedor);
        TextView total = rowView.findViewById(R.id.txtTotalFormasPagoVendedor);

        cantidad.setText(maintitle[position]);
        formasPago.setText(subtitle[position]);
        total.setText(calculo[position]);

        return rowView;
    }
}

