package com.corpogas.corpoapp.Productos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.VentaCombustible.FormasPago;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
    implements  View.OnClickListener{
    ArrayList<MainModel> mainModels;

    private View.OnClickListener listener;
    Context context;
    Activity myActivity;
    String ipEstacion, usuarioid, usuarioclave, sucursalId, posicioncarga, numerooperativa,lugarproviene,  NombreCompleto, EmpleadoNumero;
    Double MontoenCanasta;
     public MainAdapter(Activity activity, ArrayList<MainModel> mainModels, Context context, String ipEstacion, String usuarioid, String usuarioclave, String sucursalId, String posicioncarga, String numerooperativa, String lugarproviene, Double MontoenCanasta, String NombreCompleto, String EmpleadoNumero){
        this.context = context;
        this.mainModels = mainModels;
        this.myActivity = activity;
        this.ipEstacion= ipEstacion;
        this.usuarioid = usuarioid;
        this.usuarioclave=usuarioclave;
        this.sucursalId = sucursalId;
        this.posicioncarga=posicioncarga;
        this.numerooperativa=numerooperativa;
        this.lugarproviene=lugarproviene;
        this.MontoenCanasta =MontoenCanasta;
        this.NombreCompleto=NombreCompleto;
        this.EmpleadoNumero=EmpleadoNumero;
     }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Colocar el logo
        holder.imageView.setImageResource(mainModels.get(position).getlangLogo());
        holder.textView.setText(mainModels.get(position).getLangName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        VaciaCarrito();
                        break;
                    case 1:
                        FinalizaVenta();
                        break;
                    case 2:
                        Imprimir();
                    break;
                    case 3:
                        //ContinuarVenta();
                    break;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
         this.listener=listener;
    }
    @Override
    public void onClick(View view) {
        if (listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Inicializa Variables
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Asignamos variables
            imageView = itemView.findViewById(R.id.ad_image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    private void VaciaCarrito(){
        String titulo = "Vaciar Carrito";
        String mensajes = "Estas seguro de VACIAR EL CARRITO?";
        Modales modales = new Modales(myActivity);
        View viewLectura = modales.MostrarDialogoAlerta(context, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://"+ipEstacion+"/CorpogasService/api/ventaProductos/BorraProductos/sucursal/"+sucursalId+"/usuario/"+usuarioid+"/posicionCarga/"+posicioncarga;
                // Utilizamos el metodo Post para validar la contraseña
                StringRequest eventoReq = new StringRequest(Request.Method.DELETE,url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //Se instancia la respuesta del json
                                    JSONObject validar = new JSONObject(response);
                                    String correcto = validar.getString("Correcto");
                                    String mensaje = validar.getString("Mensaje");
                                    String objetorespuesta = validar.getString("ObjetoRespuesta");

                                    mensaje = "Se vacio el carrito";
                                    final Modales modales = new Modales(myActivity);
                                    View view1 = modales.MostrarDialogoCorrecto(context,mensaje);
                                    view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(context, Menu_Principal.class);
                                            myActivity.startActivity(intent);
                                            modales.alertDialog.dismiss();
                                        }
                                    });
                                } catch (JSONException e) {
                                    //herramienta  para diagnostico de excepciones
                                    //e.printStackTrace();
                                    String titulo = "AVISO";
                                    String mensaje = "Error en el borrado";
                                    Modales modales = new Modales(myActivity);
                                    View view1 = modales.MostrarDialogoAlertaAceptar(context,mensaje,titulo);
                                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            modales.alertDialog.dismiss();
                                        }
                                    });
                                }
                            }
                            //funcion para capturar errores
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        //VolleyLog.e("Error: ", volleyError.getMessage());
                        String algo = new String(error.networkResponse.data) ;
                        try {
                            //creamos un json Object del String algo
                            JSONObject errorCaptado = new JSONObject(algo);
                            //Obtenemos el elemento ExceptionMesage del errro enviado
                            String errorMensaje = errorCaptado.getString("ExceptionMessage");
                            try {
                                String titulo = "AVISO";
                                String mensaje = "" + errorMensaje;
                                Modales modales = new Modales(myActivity);
                                View view1 = modales.MostrarDialogoAlertaAceptar(context,mensaje,titulo);
                                view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modales.alertDialog.dismiss();
                                        Intent intent = new Intent(context, Menu_Principal.class);
                                        myActivity.startActivity(intent);
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // Añade la peticion a la cola
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                eventoReq.setRetryPolicy(new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(eventoReq);
                //-------------------------Aqui termina el volley --------------
            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void FinalizaVenta(){
        String titulo = "FINALIZAR VENTA";
        String mensajes = "Estas seguro de FINALIZAR LA VENTA?";
        Modales modales = new Modales(myActivity);
        View viewLectura = modales.MostrarDialogoAlerta(context, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Utilizamos el metodo POST para  finalizar la Venta
                String url = "http://" + ipEstacion + "/CorpogasService/api/Transacciones/finalizaVenta/sucursal/"+sucursalId+"/posicionCarga/"+posicioncarga+"/usuario/"+EmpleadoNumero;
                StringRequest eventoReq = new StringRequest(Request.Method.POST,url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject respuesta = new JSONObject(response);
                                    String correcto = respuesta.getString("Correcto");
                                    String mensaje = respuesta.getString("Mensaje");
                                    String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                                    if (objetoRespuesta.equals("null")) {
                                        try {
                                            String titulo = "AVISO";
                                            //String mensaje = "No hay Posiciones de Carga para Finalizar Venta";
                                            Modales modales = new Modales(myActivity);
                                            View view1 = modales.MostrarDialogoAlertaAceptar(context,mensaje,titulo);
                                            view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    Intent intent1 = new Intent(context, Menu_Principal.class);
                                                    myActivity.startActivity(intent1);
                                                    myActivity.finish();
                                                }
                                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }else {
                                        try {
                                            String titulo = "AVISO";
                                            String mensajes = "Venta Finalizada";
                                            final Modales modales = new Modales(myActivity);
                                            View view1 = modales.MostrarDialogoCorrecto(context,mensajes);
                                            view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    modales.alertDialog.dismiss();
                                                    Intent intent = new Intent(context, Menu_Principal.class);
                                                    myActivity.startActivity(intent);
                                                    myActivity.finish();
                                                }
                                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        // Colocar parametros para ingresar la  url
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }
                };
                // Añade la peticion a la cola
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                eventoReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(eventoReq);
            }
        });
        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modales.alertDialog.dismiss();
            }
        });
    }

    private void Imprimir(){
        String titulo = "IMPRIMIR";
        String mensajes = "Estas seguro de IMPRIMIR venta?";
        Modales modales = new Modales(myActivity);
        View viewLectura = modales.MostrarDialogoAlerta(context, mensajes,  "SI", "NO");
        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (numerooperativa){
                    case "5"://Tanquelleno
//                        ImprimeTanquelleno();
                        break;
                    case "16": //PuntadaRedimir
//                        ImprimePuntadaRedimir();
                        break;
                    case "1": //              Operativa normal
                    case "3":
                    case "20"://Operativa Puntada P
                        Intent intent = new Intent(context, FormasPago.class);
                        //intent.putExtra("estacionjarreo", Estacionjarreo);
                        intent.putExtra("clavedespachador", usuarioclave);
                        intent.putExtra("posicionCarga",posicioncarga);
                        intent.putExtra("IdOperativa", numerooperativa);
                        intent.putExtra("IdUsuario", usuarioid);
                        intent.putExtra("nombrecompleto", NombreCompleto);
                        intent.putExtra("estacionjarreo", "");
                        intent.putExtra("montoenCanasta", MontoenCanasta);
                        intent.putExtra("clavedespachador", usuarioclave);
                        intent.putExtra("numeroempleadosucursal", EmpleadoNumero);
                        myActivity.startActivity(intent);
                        myActivity.finish();
                }
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
//        String mensajes;
//        String titulo = "CONTINUAR VENTA";
//
//        if (lugarproviene.equals("SoloProductos")) {
//            mensajes = "Estas seguro de AGREGAR  más productos?";
//        }
//        else{
//            mensajes = "Estas seguro de AGREGAR  un Combustible?";
//        }
//        Modales modales = new Modales(myActivity);
//        View viewLectura = modales.MostrarDialogoAlerta(context, mensajes,  "SI", "NO");
//        viewLectura.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Envia a Ventas Productos
//                Intent intente = new Intent(context, VentasProductos.class);
//                if (lugarproviene.equals("SoloProductos")) {
//                    //se envia el id seleccionado a la clase Usuario Producto
//                    intente.putExtra("posicion", posicioncarga);
//                    intente.putExtra("usuario", usuarioid);
//                    intente.putExtra("cadenaproducto", "");
//                    intente.putExtra("lugarproviene", "SoloProductos");
//                    intente.putExtra("numeroOperativa", numerooperativa);
//                    myActivity.startActivity(intente);
//                    //Finaliza activity
//                    myActivity.finish();
//                }else{
//                    if (lugarproviene.equals("Predeterminado")) {
//                        //Se llama la clase para la clave del usuario
//                        Intent intent = new Intent(context, eligeLitrosPrecio.class);
//                        //se envia el id seleccionado a la clase Usuario Producto
//                        intent.putExtra("carga", posicioncarga);
//                        intent.putExtra("clave", usuarioclave);
//                        intent.putExtra("usuarioid", usuarioid);
//                        //Ejecuta la clase del Usuario producto
//                        myActivity.startActivity(intent);
//                        //Finaliza activity
//                        myActivity.finish();
//
//                    }else{
//                        solicitadespacho();
//                    }
//                }
//                modales.alertDialog.dismiss();
//            }
//        });
//        viewLectura.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                modales.alertDialog.dismiss();
//            }
//        });
    }

    private void solicitadespacho() {

        String url = "http://" + ipEstacion + "/CorpogasService/api/despachos/autorizaDespacho/posicionCargaId/" + posicioncarga + "/usuarioId/" + EmpleadoNumero;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuesta = new JSONObject(response);
                    String correctoautoriza = respuesta.getString("Correcto");
                    String mensajeautoriza = respuesta.getString("Mensaje");
                    String objetoRespuesta = respuesta.getString("ObjetoRespuesta");
                    if (correctoautoriza.equals("true")) {
                        String titulo = "AVISO";
                        String mensaje = "Listo para Iniciar Despacho";
                        final Modales modales = new Modales(myActivity);
                        View view1 = modales.MostrarDialogoCorrecto(context,mensaje);
                        view1.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                modales.alertDialog.dismiss();
                                Intent intent1 = new Intent(context, Menu_Principal.class);
                                myActivity.startActivity(intent1);
                                myActivity.finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}
