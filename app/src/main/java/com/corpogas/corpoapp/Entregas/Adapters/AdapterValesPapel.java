package com.corpogas.corpoapp.Entregas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.R;

import java.util.List;


public class AdapterValesPapel extends ArrayAdapter<PaperVoucherType> {

    public AdapterValesPapel(Context context, List<PaperVoucherType> menuCortes)
    {
        super(context, 0,menuCortes );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vales_papel_spinner_row,parent,false);

        }
        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textView = convertView.findViewById(R.id.text_view_name);
        PaperVoucherType menuCorte = getItem(position);

        if(menuCorte != null)
        {
            imageViewFlag.setImageResource(menuCorte.getImage());
            textView.setText(menuCorte.getDescription());
        }


        return convertView;

    }
}
