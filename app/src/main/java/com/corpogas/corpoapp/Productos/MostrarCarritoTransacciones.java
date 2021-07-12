package com.corpogas.corpoapp.Productos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.Adapters.RVAdapter;
import com.corpogas.corpoapp.Adapters.RVAdapterItem;
import com.corpogas.corpoapp.ConexionInternet.ComprobarConexionInternet;
import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Classes.RecyclerViewHeaders;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.FormasPago.FormasDePago;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.ObtenerClave.ClaveEmpleado;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Request.Interfaces.EndPoints;
import com.corpogas.corpoapp.SplashEmpresas.Splash;
import com.corpogas.corpoapp.VentaCombustible.ProcesoVenta;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MostrarCarritoTransacciones extends AppCompatActivity {
    RecyclerView rcvProductosCarrito;
    List<RecyclerViewHeaders> lProductosCarrito;
    RecyclerView rcvCancelarCarrito;
    List<RecyclerViewHeaders> lCancelarCarrito;
    SQLiteBD db;
    String EstacionId, sucursalId, ipEstacion,usuarioclave, cadenaproducto,
            lugarproviene, NombreCompleto;
    RespuestaApi<List<ProductoTarjetero>> respuestaApiProductoTarjetero;
    TextView txtTotalProductos, txtPosicionCarga;
    long posicioncarga,numeroOperativa,cargaNumeroInterno,usuarioid,EmpleadoNumero;
    SQLiteBD data;
    RespuestaApi<Transaccion> respuestaApiTransaccion;
    RespuestaApi<Boolean> respuestaApiAutorizaDespacho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_carrito_transacciones);
        init();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvProductosCarrito.setLayoutManager(linearLayoutManager);
        rcvProductosCarrito.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerCancelarProducto = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvCancelarCarrito.setLayoutManager(linearLayoutManagerCancelarProducto);
        rcvCancelarCarrito.setHasFixedSize(true);
//        rcvProductosCarrito.getDecoratedBoundsWithMargins();
        initializeDataCarrito();
        initializeAdapterCarrito();
        initialDataOpcionesCarrito();
        initializeAdapterOpcionesCarrito();

    }

    private void init() {
        db = new SQLiteBD(getApplicationContext());
        rcvProductosCarrito = findViewById(R.id.rcvProductosCarrito);
        rcvCancelarCarrito = findViewById(R.id.rcvCancelarCarrito);
        txtPosicionCarga = findViewById(R.id.txtPosicionCarga);
        txtTotalProductos = findViewById(R.id.txtTotalProducto);

        this.setTitle(db.getRazonSocial());
        this.setTitle(db.getNombreEstacion() + " ( EST.:" + db.getNumeroEstacion() + ")");
        EstacionId = db.getIdEstacion();
        sucursalId = db.getIdSucursal();
        ipEstacion = db.getIpEstacion();

        posicioncarga = getIntent().getLongExtra("posicionCarga",0);
        cargaNumeroInterno = getIntent().getLongExtra("cargaNumeroInterno",0);
        usuarioid = getIntent().getLongExtra("IdUsuario",0);
        cadenaproducto = getIntent().getStringExtra("cadenaproducto");
        lugarproviene = getIntent().getStringExtra("lugarproviene");
        numeroOperativa = getIntent().getLongExtra("numeroOperativa",0);
        usuarioclave = getIntent().getStringExtra("clave");
        NombreCompleto = getIntent().getStringExtra("nombrecompleto");
        EmpleadoNumero = getIntent().getLongExtra("numeroEmpleado",0);
        respuestaApiProductoTarjetero = (RespuestaApi<List<ProductoTarjetero>>) getIntent().getSerializableExtra("cadenarespuesta");
        data = new SQLiteBD(getApplicationContext());
//        cadenaRespuesta =    //getIntent().getStringExtra("cadenarespuesta");

    }


    private void initializeDataCarrito() {
        lProductosCarrito = new ArrayList<>();
        double totalPrecio = 0;
        for (ProductoTarjetero item: respuestaApiProductoTarjetero.getObjetoRespuesta())
        {
            totalPrecio += item.getPrecio();
            lProductosCarrito.add(new RecyclerViewHeaders(item.getDescripcion(),"Cantidad: "+(int)item.getCantidad()+"\nPrecio: $"+String.format("%.2f",item.getPrecio()),R.drawable.atf900));
        }
        txtTotalProductos.setText("Total: $"+String.format("%.2f",totalPrecio));
        txtPosicionCarga.setText("Posicion: "+ cargaNumeroInterno);


//        lProductosCarrito.add(new RecyclerViewHeaders("ATF DEXRON II (900 ML)","Cantidad: 1 \nPrecio: $60",R.drawable.atf900));
//        lProductosCarrito.add(new RecyclerViewHeaders("TOP OIL LIMPIEZA CONTINUA 200 ML","Cantidad: 1 \nPrecio: $35",R.drawable.oil200ml));
    }

    private void initialDataOpcionesCarrito() {

        lCancelarCarrito = new ArrayList<>();
        lCancelarCarrito.add(new RecyclerViewHeaders("Eliminar","",R.drawable.vaciarproductos));
        lCancelarCarrito.add(new RecyclerViewHeaders("Finalizar","",R.drawable.finaliza));
        lCancelarCarrito.add(new RecyclerViewHeaders("Imprimir","",R.drawable.imprimir));
        lCancelarCarrito.add(new RecyclerViewHeaders("Combustible","",R.drawable.agregarproductos));

    }





    private void initializeAdapterCarrito() {
        RVAdapter adapter = new RVAdapter(lProductosCarrito);
//        adapter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lProductosCarrito.get(rcvProductosCarrito.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
//            }
//        });

        rcvProductosCarrito.setAdapter(adapter);
    }

    private void initializeAdapterOpcionesCarrito() {
        RVAdapterItem adapter = new RVAdapterItem(lCancelarCarrito);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo;
//                Toast.makeText(getApplicationContext(),"Seleccion :" + lCancelarCarrito.get(rcvCancelarCarrito.getChildAdapterPosition(v)).getTitulo(), Toast.LENGTH_SHORT).show();
                titulo = lCancelarCarrito.get(rcvCancelarCarrito.getChildAdapterPosition(v)).getTitulo();
                if(titulo.equals("Eliminar"))
                {
                    VaciaCarrito();
                }
                else if (titulo.equals("Finalizar"))
                {
                    FinalizaVenta();
                }
                else if(titulo.equals("Imprimir"))
                {
                    Imprimir();
//                    Toast.makeText(getApplicationContext(),"En proceso de desarrollo", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ContinuarVenta();
                }
            }
        });

        rcvCancelarCarrito.setAdapter(adapter);
    }

    private void VaciaCarrito() {
        String titulo = "Vaciar Carrito";
        String mensajes = "Estas seguro de VACIAR EL CARRITO?";
        Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes, "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints eliminarProductos = retrofit.create(EndPoints.class);

                Call<ResponseBody>deleteRequest  = eliminarProductos.deleteProductos(sucursalId,usuarioid,posicioncarga);
                deleteRequest.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            String titulo = "AVISO";
                            String mensaje = "Error en el borrado";
                            Modales modales = new Modales(MostrarCarritoTransacciones.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this, mensaje, titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                }
                            });
                            return;
                        }
                        String mensaje = "Se vacio el carrito";
                        final Modales modales = new Modales(MostrarCarritoTransacciones.this);
                        View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this, mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                                modales.alertDialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String titulo = "AVISO";
                        String mensaje = t.getMessage();
                        Modales modales = new Modales(MostrarCarritoTransacciones.this);
                        View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this, mensaje, titulo);
                        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }
                });


            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void FinalizaVenta() {
        String mensajes = "Estas seguro de finalizar la venta?";
        Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes, "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                EndPoints postFinalizaVenta = retrofit.create(EndPoints.class);
                Call<RespuestaApi<Transaccion>> call = postFinalizaVenta.getPostFinalizaVenta(sucursalId,posicioncarga,usuarioid);
                call.enqueue(new Callback<RespuestaApi<Transaccion>>() {


                    @Override
                    public void onResponse(Call<RespuestaApi<Transaccion>> call, Response<RespuestaApi<Transaccion>> response) {
                        if (!response.isSuccessful()) {
                            return;
                        }
                        respuestaApiTransaccion = response.body();
                        String mensaje = respuestaApiTransaccion.getMensaje();   //respuesta.getString("Mensaje");
//                Transaccion objetoRespuesta = respuestaApiTransaccion.getObjetoRespuesta(); //respuesta.getString("ObjetoRespuesta");
                        if(respuestaApiTransaccion.getObjetoRespuesta() == null)
                        {
                            String titulo = "AVISO";
                            //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                            Modales modales = new Modales(MostrarCarritoTransacciones.this);
                            View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this,mensaje,titulo);
                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            });

                        }else
                        {
                            String titulo = "AVISO";
                            String mensajes = "Venta Finalizada";
                            final Modales modales = new Modales(MostrarCarritoTransacciones.this);
                            View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this,mensajes);
                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    modales.alertDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Menu_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure(Call<RespuestaApi<Transaccion>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void ContinuarVenta() {
        String mensajes;

        if (lugarproviene.equals("SoloProductos")) {
            mensajes = "Estas seguro de AGREGAR  más productos?";
        }
        else{
            mensajes = "Estas seguro de AGREGAR  un Combustible?";
        }
        Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lugarproviene.equals("SoloProductos")) {
                    //Envia a Ventas Productos
//                    Intent intente = new Intent(getApplicationContext(), VentasProductos.class);
//                    //se envia el id seleccionado a la clase Usuario Producto
//                    intente.putExtra("posicion", posicioncarga);
//                    intente.putExtra("usuario", usuarioid);
//                    intente.putExtra("cadenaproducto", "");
//                    intente.putExtra("lugarproviene", "SoloProductos");
//                    intente.putExtra("numeroOperativa", numerooperativa);
//                    startActivity(intente);
//                    //Finaliza activity
//                    finish();
                }else{
                    if (lugarproviene.equals("Predeterminado")) {
//                        //Se llama la clase para la clave del usuario
//                        Intent intent = new Intent(getApplicationContext(), eligeLitrosPrecio.class);
//                        //se envia el id seleccionado a la clase Usuario Producto
//                        intent.putExtra("carga", posicioncarga);
//                        intent.putExtra("clave", usuarioclave);
//                        intent.putExtra("usuarioid", usuarioid);
//                        //Ejecuta la clase del Usuario producto
//                        startActivity(intent);
//                        //Finaliza activity
//                        finish();

                    }else{
                        solicitadespacho();
                    }
                }
                modales.alertDialog.dismiss();
            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });

    }

    private void Imprimir() {
        String mensajes = "Estas seguro de IMPRIMIR venta?";
        Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View viewLectura = modales.MostrarDialogoAlerta(MostrarCarritoTransacciones.this, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), FormasDePago.class);
                startActivity(intent);
                finish();

            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void solicitadespacho() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + data.getIpEstacion() + "/CorpogasService/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints AutorizaDespacho = retrofit.create(EndPoints.class);
        Call<RespuestaApi<Boolean>> call = AutorizaDespacho.getAutorizaDespacho(posicioncarga,EmpleadoNumero);
        call.enqueue(new Callback<RespuestaApi<Boolean>>() {


            @Override
            public void onResponse(Call<RespuestaApi<Boolean>> call, Response<RespuestaApi<Boolean>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                respuestaApiAutorizaDespacho = response.body();
                boolean correctoautoriza =  respuestaApiAutorizaDespacho.isCorrecto();

                if (correctoautoriza ==true) {
                    enviaMunu();
                } else {
                    String titulo = "AVISO";
                    String mensaje = "La posición de carga se encuentra Ocupada";
                    Modales modales = new Modales(MostrarCarritoTransacciones.this);
                    View view1 = modales.MostrarDialogoAlertaAceptar(MostrarCarritoTransacciones.this,mensaje,titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<Boolean>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviaMunu() {
        String titulo = "AVISO";
        String mensaje = "Listo para Iniciar Despacho";
        final Modales modales = new Modales(MostrarCarritoTransacciones.this);
        View view1 = modales.MostrarDialogoCorrecto(MostrarCarritoTransacciones.this,mensaje);
        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
                Intent intent1 = new Intent(getApplicationContext(), Menu_Principal.class);
                startActivity(intent1);
                finish();
            }
        });
    }


}
