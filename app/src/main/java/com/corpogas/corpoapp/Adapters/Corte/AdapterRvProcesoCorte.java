package com.corpogas.corpoapp.Adapters.Corte;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Fragments.Corte.CombustiblesFragment;
import com.corpogas.corpoapp.Fragments.Corte.DesgloseValesFragment;
import com.corpogas.corpoapp.Fragments.Corte.FajillasBilletesFragment;
import com.corpogas.corpoapp.Fragments.Corte.FajillasMonedasFragment;
import com.corpogas.corpoapp.Fragments.Corte.FormasPagoFragment;
import com.corpogas.corpoapp.Fragments.Corte.GranTotalFragment;
import com.corpogas.corpoapp.Fragments.Corte.LecturasMecanicasFragment;
import com.corpogas.corpoapp.Corte.Fragments.PicosFragment;
import com.corpogas.corpoapp.Fragments.Corte.ProcesoCorteFragment;
import com.corpogas.corpoapp.Fragments.Corte.ProductosFaltantesFragment;
import com.corpogas.corpoapp.Fragments.Corte.ProductosFragment;
import com.corpogas.corpoapp.Fragments.Corte.ValesPapelFragment;
import com.corpogas.corpoapp.Entities.Cortes.CierreDespachoDetalle;
import com.corpogas.corpoapp.Entities.Cortes.MenuCorte;
import com.corpogas.corpoapp.Entities.Cortes.ProductosFaltantes;
import com.corpogas.corpoapp.Entities.Cortes.ValePapelDenominacion;
import com.corpogas.corpoapp.R;

import java.util.ArrayList;

public class AdapterRvProcesoCorte extends RecyclerView.Adapter<AdapterRvProcesoCorte.RViewHolder>{
    final static String DATA_DESGLOCEVALES = "desgloceVales";
    final static String DATA_PRODUCTOSFALTANTES = "productosfaltantes";
    final static String DATA_FAJILLAS_BILLETES_COMPLETADO = "fajillasBilletesCompletado";
    final static String DATA_FAJILLAS_MONEDAS_COMPLETADO = "fajillasMonedasCompletado";
    final static String DATA_PICOS_COMPLETADO = "picosCompletado";
    final static String DATA_DINERO_FAJILLAS_MONEDAS = "dineroFajillasMonedas";
    final static String DATA_DESPACHO_DETALLE = "despachoDetalle";

    private ArrayList<MenuCorte> itemsMenuCorte;
    Activity activity;
    int row_index = -1;

    FragmentTransaction transaction;
    Fragment fragmentProcespCorte,fragmentLecturasMecanicas,fragmentProductos, fragmentProductosFaltantes,
            fragmentFajillasBilletes, fragmentFajillasMonedas, fragmentPicos, fragmentValesPapel, fragmentDesgloseVales,
            fragmentFormasPago,fragmentCombustibles,fragmentGranTotal;
    Bundle bundleEnvio;
    ArrayList<ValePapelDenominacion> lCarritoValePapelDenominacion = new ArrayList<ValePapelDenominacion>();
    ArrayList<ProductosFaltantes> lProductosFaltantes = new ArrayList<ProductosFaltantes>();
    ArrayList<CierreDespachoDetalle> lCierreDespachoDetalle = new ArrayList<>();
    Boolean fajillasBilletesProcesoCompletado;
    Boolean fajillasMonedasProcesoCompletado;
    Boolean picosProcesoCompleado;
    int cantidadDineroFajillasMonedas;

    public AdapterRvProcesoCorte(ArrayList<MenuCorte> itemsMenuCorte, Activity activity) {
        this.itemsMenuCorte = itemsMenuCorte;
        this.activity = activity;
        fragmentProcespCorte = new ProcesoCorteFragment();
        fragmentLecturasMecanicas = new LecturasMecanicasFragment();
        fragmentProductos = new ProductosFragment();
        fragmentProductosFaltantes = new ProductosFaltantesFragment();
        fragmentFajillasBilletes = new FajillasBilletesFragment();
        fragmentFajillasMonedas = new FajillasMonedasFragment();
        fragmentPicos = new PicosFragment();
        fragmentValesPapel = new ValesPapelFragment();
        fragmentDesgloseVales = new DesgloseValesFragment();
        fragmentFormasPago = new FormasPagoFragment();
        fragmentCombustibles = new CombustiblesFragment();
        fragmentGranTotal = new GranTotalFragment();
        bundleEnvio = new Bundle();


        ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment,fragmentProcespCorte).commit();

    }

    public void RecuperaDatosVales(ArrayList<ValePapelDenominacion> valePapelDenominacions)
    {
        lCarritoValePapelDenominacion = valePapelDenominacions;
    }

    public void ProductosFaltantes(ArrayList<ProductosFaltantes> productosFaltantes)
    {
        lProductosFaltantes = productosFaltantes;
    }

    public void RecuperaCierreDespachoDetalle(ArrayList<CierreDespachoDetalle> cierreDespachoDetalles){
        lCierreDespachoDetalle = cierreDespachoDetalles;

    }


    public void FajillaBilletesCompletado (Boolean fajillasBilletesCompletado){
        fajillasBilletesProcesoCompletado = fajillasBilletesCompletado;
    }

    public void FajillaMonedasCompletado(Boolean fajillasMonedasCompletado, int dineroFajillasMonedas){
        fajillasMonedasProcesoCompletado = fajillasMonedasCompletado;
        cantidadDineroFajillasMonedas = dineroFajillasMonedas;
    }

    public void PicosCompletado(Boolean picosCompletado){
        picosProcesoCompleado = picosCompletado;
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_corte, parent, false);
        RViewHolder rViewHolder = new RViewHolder(view);
        return rViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        MenuCorte currentItem = itemsMenuCorte.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;

                transaction =  ((FragmentActivity)activity).getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:
                        transaction.replace(R.id.contenedorFragment,fragmentLecturasMecanicas);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Lecturas mecanicas",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        transaction.replace(R.id.contenedorFragment,fragmentProductos);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Productos",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        bundleEnvio.putSerializable(DATA_PRODUCTOSFALTANTES,lProductosFaltantes);
                        fragmentProductosFaltantes.setArguments(bundleEnvio);
                        transaction.replace(R.id.contenedorFragment,fragmentProductosFaltantes);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Productos Faltantes",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        transaction.replace(R.id.contenedorFragment,fragmentFajillasBilletes);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Fajillas Billetes",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        transaction.replace(R.id.contenedorFragment,fragmentFajillasMonedas);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Fajillas Monedas",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        transaction.replace(R.id.contenedorFragment,fragmentPicos);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Formas de picos",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        transaction.replace(R.id.contenedorFragment,fragmentValesPapel);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Vales de papel",Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        bundleEnvio.putSerializable(DATA_DESGLOCEVALES,lCarritoValePapelDenominacion);
                        fragmentDesgloseVales.setArguments(bundleEnvio);
                        transaction.replace(R.id.contenedorFragment,fragmentDesgloseVales);

//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Desglose Vales",Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        transaction.replace(R.id.contenedorFragment,fragmentFormasPago);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Formas Pago",Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        bundleEnvio.putSerializable(DATA_DESPACHO_DETALLE,lCierreDespachoDetalle);
                        fragmentCombustibles.setArguments(bundleEnvio);
                        transaction.replace(R.id.contenedorFragment,fragmentCombustibles);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Combustibles",Toast.LENGTH_SHORT).show();
                        break;
                    case 10:
                        bundleEnvio.putSerializable(DATA_FAJILLAS_BILLETES_COMPLETADO,fajillasBilletesProcesoCompletado);
                        bundleEnvio.putSerializable(DATA_FAJILLAS_MONEDAS_COMPLETADO, fajillasMonedasProcesoCompletado);
                        bundleEnvio.putSerializable(DATA_DINERO_FAJILLAS_MONEDAS, cantidadDineroFajillasMonedas);
                        bundleEnvio.putSerializable(DATA_PICOS_COMPLETADO, picosProcesoCompleado);
                        fragmentGranTotal.setArguments(bundleEnvio);
                        transaction.replace(R.id.contenedorFragment,fragmentGranTotal);
//                        transaction.addToBackStack(null);
//                        Toast.makeText(activity,"Gran Total",Toast.LENGTH_SHORT).show();
                        break;
                }
                transaction.commit();


                notifyDataSetChanged();

            }
        });

        if(row_index == position)
        {
            holder.linearLayout.setBackgroundResource(R.drawable.recycler_view__corte);
        }else
        {
            holder.linearLayout.setBackgroundResource(R.drawable.recycler_view_selected);
        }

    }

    @Override
    public int getItemCount() {
        return itemsMenuCorte.size();
    }

    public static class RViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCorte);
            textView = itemView.findViewById(R.id.txtTitulo);
            linearLayout = itemView.findViewById(R.id.linearLayoutMenuCorte);
        }
    }

}
