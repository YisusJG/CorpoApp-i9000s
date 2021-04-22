package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;

public class Menu_Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);
        SQLiteBD data = new SQLiteBD(getApplicationContext());
        this.setTitle(data.getNombreEsatcion());
    }

    public void onClickMenuPrincipal(View v) {
        Intent intent;
        switch (v.getId()) {


            case R.id.btnImgVentas:
//                intent = new Intent(getApplicationContext(), ventash.class);
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                break;
            case R.id.btnImgTickets:
//                intent = new Intent(getApplicationContext(), despachdorclave.class); //despachdorclave
//                intent.putExtra("LugarProviene", "Impresion");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);

                break;
            case R.id.btnImgMonederos:
//                intent = new Intent(getApplicationContext(), MonederosElectronicos.class);
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                intent.putExtra("Enviadodesde", "menuprincipal");
//                startActivity(intent);
                //finish();
//                    CardReaderTypeEnum cardType = MAG_CARD; // aqui va la seccion de tarjeta
//                    CardFragment cf = new CardFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("card_type", cardType);
//                    cf.setArguments(bundle);
//                    getFragmentManager().beginTransaction().replace(R.id.menu, cf).commit();
                break;
            case R.id.btnImgProductos:
//                intent = new Intent(getApplicationContext(), claveProducto.class); //claveProducto
//                intent.putExtra("LugarProviene", "Productos");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgCortes:
//                intent = new Intent(getApplicationContext(), Clave.class);
//                intent.putExtra("LugarProviene", "Cortes");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgPendientes:
//                intent = new Intent(getApplicationContext(), claveUPendientes.class); //claveUPendientes
//                intent.putExtra("LugarProviene", "TicketPendiente");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgGastos:
//                intent = new Intent(getApplicationContext(), claveGastos.class);
//                intent.putExtra("LugarProviene", "Gastos");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgReimpresiones:
//                intent = new Intent(getApplicationContext(), despachdorclave.class);
//                intent.putExtra("LugarProviene", "Reimpresion");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;
            case R.id.btnImgFacturacion:
//                intent = new Intent(getApplicationContext(), ClaveDespachadorAcumular.class); //ClaveDespachadorAcumular
//                intent.putExtra("LugarProviene", "Facturas");
//                intent.putExtra("device_name", m_deviceName);
//                intent.putExtra("banderaHuella", banderaHuella);
//                startActivity(intent);
                //finish();
                break;

        }
    }
}