package com.corpogas.corpoapp.Facturacion.Adapters;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.device.PrinterManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Facturacion.ClienteFacturas;
import com.corpogas.corpoapp.Facturacion.Entities.RespuestaRFC;
import com.corpogas.corpoapp.Facturacion.Entities.RespuestaSolicitudFactura;
import com.corpogas.corpoapp.Facturacion.Entities.SolicitudFactura;
import com.corpogas.corpoapp.Gastos.CargaGasto;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Service.PrintBillService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacturacionAdapter extends RecyclerView.Adapter<FacturacionAdapter.FacturacionVH>{
    List<RespuestaRFC> facturacionList;
    Context mContext; // Declara aquí
    ClienteFacturas clienteFacturas;
    RespuestaRFC respuestaRFCDatos;
    SQLiteBD db;
    String idUsuario, ipEstacion;
    Long id;

    public FacturacionAdapter(List<RespuestaRFC> respuestaRFCList, Context context, ClienteFacturas clienteFacturas, SQLiteBD db,String idUsuario) {
        this.facturacionList = respuestaRFCList;
        this.mContext = context;
        this.clienteFacturas = clienteFacturas;
        this.db = db;
        this.idUsuario = idUsuario;

    }

    @NonNull
    @Override
    public FacturacionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowfacturacion, parent, false);
        return new FacturacionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturacionVH holder, int position) {
        RespuestaRFC respuestaRFC = facturacionList.get(position);
        holder.razsonSocialTxt.setText(respuestaRFC.getRazonSocial());
        holder.emailTxt.setText("Email: " + respuestaRFC.getEmail());
        holder.idClienteTxt.setText("IdCliente: " + respuestaRFC.getIdCliente());
        holder.facturarBtn.setText("Facturar");

        boolean isExpandable = facturacionList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        clienteFacturas.bar.cancel();

    }

    @Override
    public int getItemCount() {
        return facturacionList.size();
    }

    public class FacturacionVH extends RecyclerView.ViewHolder {
        TextView razsonSocialTxt, emailTxt, idClienteTxt;
        Button facturarBtn;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        SolicitudFactura solicitudFactura;
        EditText txtNumRastreo;
        String numeroRastreo;
        RespuestaApi<RespuestaSolicitudFactura> respuestaCFDI;
        Type respuestaSoicitudFactura;
        ProgressDialog bar;


        //variables scaneo
        private Button btnNumRastreo,btnStartScan;
        private BroadcastReceiver mScanRecevier = null;

        public static final int ENCODE_MODE_UTF8 = 1;
        public static final int ENCODE_MODE_GBK = 2;
        public static final int ENCODE_MODE_NONE = 3;


        // Variable para Imprimir
        //private PrinterManager printer = new PrinterManager();
        private final static String STR_PRNT_SALE = "sale";

        //termina aqui


        //Variables para probar el llenado de list
        private ListView list;
        private ArrayAdapter<String> adapter;
        private ArrayList<String> lstNumeroRastreo;

        //
        public FacturacionVH (@NonNull View itemView) {
            super(itemView);
            razsonSocialTxt = itemView.findViewById(R.id.razon_social);
            emailTxt = itemView.findViewById(R.id.email);
            idClienteTxt = itemView.findViewById(R.id.id_cliente);
            facturarBtn = itemView.findViewById(R.id.btnFacturar);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);


            linearLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    respuestaRFCDatos = facturacionList.get(getAdapterPosition());
                    respuestaRFCDatos.setExpandable(!respuestaRFCDatos.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            facturarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tituloHeader = "INGRESA NUMERO DE RASTREO";
//                    String mensaje = "Ingresa cantidad: " ;
                    Modales modales = new Modales(clienteFacturas);
                    View viewFactura = modales.MostrarDialogoInsertaFactura(clienteFacturas, tituloHeader);
                    txtNumRastreo = ((EditText) viewFactura.findViewById(R.id.textInsertarDato));
                    btnNumRastreo = ((Button) viewFactura.findViewById(R.id.btnAgregaRastreo));
                    btnStartScan =((Button) viewFactura.findViewById(R.id.btnScan));
                    list = ((ListView) viewFactura.findViewById(R.id.listViewRastreo));
                    // Es una implemntación de la lista respaldado por un array
                    // es decir en este array se colocan los datos que después se pasan al adaptador
                    // para ser mostrados

                    lstNumeroRastreo = new ArrayList<String>();

                    // provee de datos a un control de selección a partir de un array de objetos de cualquier tipo.
                    adapter = new ArrayAdapter<String>(clienteFacturas, R.layout.factura_num_rastreo, lstNumeroRastreo);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(clienteFacturas, "Numero de rastreo eliminado", Toast.LENGTH_SHORT).show();
                            lstNumeroRastreo.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });

//                    txtNumRastreo.addTextChangedListener(new TextWatcher() {
//                        int len=0;
//
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                            String str = txtNumRastreo.getText().toString();
//                            len = str.length();
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//
//                            String str = txtNumRastreo.getText().toString();
//                            if(str.length()==4 && len <str.length() || str.length()==9 && len <str.length() || str.length()==14 && len <str.length()){//len check for backspace
//                                txtNumRastreo.append("-");
//                            }
//
//                        }
//                    });

                    mScanRecevier = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            Log.e("Scan", "scan receive.......");
                            String numeroEstacion = db.getNumeroEstacion();
                            String scanResult = "";
                            int length = intent.getIntExtra("EXTRA_SCAN_LENGTH", 0);
                            int encodeType = intent.getIntExtra("EXTRA_SCAN_ENCODE_MODE", 1);

                            if (encodeType == ENCODE_MODE_NONE) {
                                byte[] data = intent.getByteArrayExtra("EXTRA_SCAN_DATA");
                                try {
                                    scanResult = new String(data, 0, length, "iso-8859-1");//Encode charSet
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                scanResult = intent.getStringExtra("EXTRA_SCAN_DATA");
                                String comparaNumeroRastreo = scanResult.substring(0,numeroEstacion.length());

                                if(numeroEstacion.equals(comparaNumeroRastreo))
                                {
                                    lstNumeroRastreo.add(scanResult);
                                    adapter.notifyDataSetChanged();

                                }else
                                {
                                    txtNumRastreo.setError("El número de rastreo no pertenece a la sucursal");
                                    //queda pendiente el toas aqui para validar que pertnezca a la estacion donde se saco el tricket

                                }
                            }
                        }
                    };

                    IntentFilter filter = new IntentFilter("ACTION_BAR_SCAN");
                    clienteFacturas.registerReceiver(mScanRecevier, filter);


                    btnNumRastreo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            numeroRastreo =txtNumRastreo.getText().toString();
                            String numeroEstacion = db.getNumeroEstacion();

                            if(numeroRastreo.isEmpty()){
                                txtNumRastreo.setError("Ingresa un numero de rastreo");
                            }
                            else {
                                if (numeroRastreo.length() < 5) {
                                    txtNumRastreo.setError("Verifica tu numero de rastreo");
                                } else {
                                    String comparaNumeroRastreo = numeroRastreo.substring(0, numeroEstacion.length());

                                    if (numeroEstacion.equals(comparaNumeroRastreo)) {
//                                    boolean existeNumeroRastreo = lstNumeroRastreo.contains(numeroRastreo);
                                        if (lstNumeroRastreo.contains(numeroRastreo)) {
                                            txtNumRastreo.setError("Este número de rastreo ya lo has agregado");
                                        } else {
                                            lstNumeroRastreo.add(numeroRastreo);
                                            adapter.notifyDataSetChanged();
                                            txtNumRastreo.setText(null);
                                        }


                                    } else {
                                        txtNumRastreo.setError("El número de rastreo no pertenece a la sucursal");
                                        //queda pendiente el toas aqui para validar que pertnezca a la estacion donde se saco el tricket

                                    }
                                }
                            }
                        }
                    });

                    btnStartScan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent startIntent = new Intent("ACTION_BAR_TRIGSCAN");
                            startIntent.putExtra("timeout", 60);// Units per second,and Maximum 9
//                            txtNumRastreo.setText("Start Scan...");

                            clienteFacturas.sendBroadcast(startIntent);

                        }
                    });

                    viewFactura.findViewById(R.id.btncancelarFactura).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();

                        }
                    });

                    viewFactura.findViewById(R.id.btnEnviarFactura).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clienteFacturas.ObtenProgressDialog("Enviando Factura al correo...");
                            solicitudFactura = new SolicitudFactura();
                            solicitudFactura.setDispositivoId(db.getIdTarjtero());
                            solicitudFactura.setTickets(lstNumeroRastreo);
                            solicitudFactura.setRfc(respuestaRFCDatos.getRFC());
                            solicitudFactura.setEmail(respuestaRFCDatos.getEmail()+"jesus.gomez@corpogas.com.mx,miguel.reyes@corpogas.com.mx,amairani.delgado@corpogas.com.mx"); // "jesus.gomez@corpogas.com.mx,miguel.reyes@corpogas.com.mx,amairani.delgado@corpogas.com.mx"
                            //solicitudFactura.setEmail("abinadab.vazquez@gmail.com");
                            solicitudFactura.setIdCliente(respuestaRFCDatos.getIdCliente());
                            solicitudFactura.setIdAlias(db.getIdTarjtero());
                            ipEstacion = db.getIpEstacion();
                            id = Long.parseLong(db.getIdSucursal());
                            String json = new Gson().toJson(solicitudFactura);

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            EndPoints solicitarFactura = retrofit.create(EndPoints.class);
                            Call<RespuestaApi<RespuestaSolicitudFactura>> call = solicitarFactura.postSolicitarFactura(id,Long.parseLong(idUsuario),solicitudFactura);
                            call.enqueue(new Callback<RespuestaApi<RespuestaSolicitudFactura>>() {

                                @Override
                                public void onResponse(Call<RespuestaApi<RespuestaSolicitudFactura>> call, Response<RespuestaApi<RespuestaSolicitudFactura>> response) {
                                    if (!response.isSuccessful()) {
                                        return;
                                    }
                                    respuestaCFDI = response.body();

                                    if(respuestaCFDI.isCorrecto() == false)
                                    {
                                        String validaRespuestaFacturacion = respuestaCFDI.getAlertaHttp().getError().getMensajeSistema();
                                        clienteFacturas.bar.cancel();
//                                        Toast.makeText(mContext, validaRespuestaFacturacion, Toast.LENGTH_LONG).show();

                                        Snackbar.make(view,validaRespuestaFacturacion, Snackbar.LENGTH_LONG).show();
//                                                txtNumRastreo.setError(validaRespuestaFacturacion);
                                    }
                                    else{
                                        Toast.makeText(mContext, respuestaCFDI.getObjetoRespuesta().getDatos().getMensaje(), Toast.LENGTH_LONG).show();
                                        enviarPrincipal();
                                    }
                                }

                                @Override
                                public void onFailure(Call<RespuestaApi<RespuestaSolicitudFactura>> call, Throwable t) {
                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

//                    viewFactura.findViewById(R.id.btnImprimirFactura).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            clienteFacturas.ObtenProgressDialog("En proceso de Facturación...");
//                            solicitudFactura = new SolicitudFactura();
//                            solicitudFactura.setDispositivoId(db.getIdTarjtero());
//                            solicitudFactura.setTickets(lstNumeroRastreo);
//                            solicitudFactura.setRfc(respuestaRFCDatos.getRFC());
//                            solicitudFactura.setEmail(respuestaRFCDatos.getEmail()+"jesus.gomez@corpogas.com.mx,miguel.reyes@corpogas.com.mx,amairani.delgado@corpogas.com.mx"); // "jesus.gomez@corpogas.com.mx,miguel.reyes@corpogas.com.mx,amairani.delgado@corpogas.com.mx"
//                            //solicitudFactura.setEmail("abinadab.vazquez@gmail.com");
//                            solicitudFactura.setIdCliente(respuestaRFCDatos.getIdCliente());
//                            solicitudFactura.setIdAlias(db.getIdTarjtero());
//                            ipEstacion = db.getIpEstacion();
//                            id = Long.parseLong(db.getIdSucursal());
////                            String json = new Gson().toJson(solicitudFactura);
//
//                            Retrofit retrofit = new Retrofit.Builder()
//                                    .baseUrl("http://"+ ipEstacion  +"/corpogasService/")//http://" + data.getIpEstacion() + "/corpogasService_Entities_token/
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build();
//
//                            EndPoints solicitarFactura = retrofit.create(EndPoints.class);
//                            Call<RespuestaApi<RespuestaSolicitudFactura>> call = solicitarFactura.postSolicitarFactura(id,Long.parseLong(idUsuario),solicitudFactura);
//                            call.enqueue(new Callback<RespuestaApi<RespuestaSolicitudFactura>>() {
//
//                                @Override
//                                public void onResponse(Call<RespuestaApi<RespuestaSolicitudFactura>> call, Response<RespuestaApi<RespuestaSolicitudFactura>> response) {
//                                    if (!response.isSuccessful()) {
//                                        return;
//                                    }
//                                    respuestaCFDI = response.body();
//
//                                    if(respuestaCFDI.isCorrecto() == false)
//                                    {
//                                        if(respuestaCFDI.getAlertaHttp() == null)
//                                        {
//                                            String mensaje = respuestaCFDI.getMensaje();
//                                            Toast.makeText(mContext, mensaje, Toast.LENGTH_LONG).show();
//                                        }else
//                                        {
//                                            String validaRespuestaFacturacion = respuestaCFDI.getAlertaHttp().getError().getMensajeSistema();
//                                            Toast.makeText(mContext, validaRespuestaFacturacion, Toast.LENGTH_LONG).show();
//                                        }
//                                        clienteFacturas.bar.cancel();
//
////                                                txtNumRastreo.setError(validaRespuestaFacturacion);
//                                    }else{
//
//                                        //Toast.makeText(mContext, "Ya Imprimió??", Toast.LENGTH_SHORT).show();
//                                        clienteFacturas.bar.cancel();
//                                        enviarPrincipal();
//                                        //doprintwork();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<RespuestaApi<RespuestaSolicitudFactura>> call, Throwable t) {
//                                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                        }
//                        @Override
//                        public void onClick(View view) {
//                            clienteFacturas.ObtenProgressDialog("En proceso de Facturación... ");
//                            new Thread(new Runnable() {
//                                public void run() {
//
//                                    try {
////                                        RFC = txtObtenRFC.getEditText().getText().toString().trim();
//                                        httpsJsonPostObtenFactura();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    clienteFacturas.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            if(respuestaCFDI.isCorrecto() == false)
//                                            {
//                                                if(respuestaCFDI.getAlertaHttp() == null)
//                                                {
//                                                    String mensaje = respuestaCFDI.getMensaje();
//                                                    Toast.makeText(mContext, mensaje, Toast.LENGTH_LONG).show();
//                                                }else
//                                                {
//                                                    String validaRespuestaFacturacion = respuestaCFDI.getAlertaHttp().getError().getMensajeSistema();
//                                                    Toast.makeText(mContext, validaRespuestaFacturacion, Toast.LENGTH_LONG).show();
//                                                }
//                                                clienteFacturas.bar.cancel();
//
////                                                txtNumRastreo.setError(validaRespuestaFacturacion);
//                                            }
//                                            else{
//
//                                                if (printThread != null && !printThread.isThreadFinished()) {
//                                                    Log.e(tag, "Thread is still running...");
//                                                    return;
//                                                }
//
//                                                printThread = new Print_Thread_CFDI(PRINT_CFDI);
//                                                printThread.start();
//
//                                            }
//                                        }
//                                    });
//                                }
//                            }).start();
//
//
////
//
////                            Toast.makeText(mContext, "a qui va la impresion", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                }
            });
        }

        void doprintwork() {
            Intent intentService = new Intent(mContext, PrintBillService.class);
            intentService.putExtra("SPRT", "expenses");
            intentService.putExtra("Subtotal", "100");
            intentService.putExtra("Iva", "16");
            intentService.putExtra("Descripcion", "pruebate");
            intentService.putExtra("NumeroTicket", "1234");
            intentService.putExtra("NombreEmpleado", "123456");
            intentService.putExtra("DescripcionGasto", "chelas");
            intentService.putExtra("TipoGasto", "Gasto");
            mContext.startActivity(intentService);
        }

        private void enviarPrincipal() {

            Intent i = new Intent(clienteFacturas, Menu_Principal.class);
            clienteFacturas.startActivity(i);
            clienteFacturas.finishAffinity();
        }

    }

}
