package com.corpogas.corpoapp.Token;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Interfaces.Endpoints.EndPoints;
import com.corpogas.corpoapp.Login.LoginActivity;
import com.corpogas.corpoapp.Menu_Principal;
import com.corpogas.corpoapp.Modales.Modales;
import com.corpogas.corpoapp.Productos.VentasProductos;
import com.corpogas.corpoapp.R;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalToken {
    public static void getToken(Activity activity) {
        SQLiteBD db = new SQLiteBD(activity);
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://"+db.getIpEstacion()+"/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(Long.parseLong(db.getIdSucursal()), db.getClave());
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, retrofit2.Response<RespuestaApi<AccesoUsuario>> response) {
                String bearerToken;
                if (response.isSuccessful()) {
                    RespuestaApi<AccesoUsuario> token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                    db.InsertarToken(bearerToken);

                    Intent intent = new Intent(activity, Menu_Principal.class);
                    activity.startActivity(intent);
                } else {
                    String titulo = "AVISO";
                    String mensaje = "Hubo un error al obtener el Token";
                    Modales modales = new Modales(activity);
                    View view1 = modales.MostrarDialogoAlertaAceptar(activity, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getAndUpdateToken(Activity activity) {
        SQLiteBD db = new SQLiteBD(activity);
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://"+db.getIpEstacion()+"/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(Long.parseLong(db.getIdSucursal()), db.getClave());
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, retrofit2.Response<RespuestaApi<AccesoUsuario>> response) {
                String bearerToken;
                if (response.isSuccessful()) {
                    RespuestaApi<AccesoUsuario> token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                    db.updateToken(bearerToken);
                } else {
                    String titulo = "AVISO";
                    String mensaje = "Hubo un error al obtener el Token";
                    Modales modales = new Modales(activity);
                    View view1 = modales.MostrarDialogoAlertaAceptar(activity, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getAndUpdateTokenforReload(Activity activity) {
        SQLiteBD db = new SQLiteBD(activity);
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://"+ip2+"/CorpogasService/") //anterior
                .baseUrl("http://"+db.getIpEstacion()+"/CorpogasService_entities_token/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EndPoints obtenerToken = retrofit.create(EndPoints.class);
        Call<RespuestaApi<AccesoUsuario>> call = obtenerToken.getAccesoUsuario(Long.parseLong(db.getIdSucursal()), db.getClave());
        call.timeout().timeout(60, TimeUnit.SECONDS);
        call.enqueue(new Callback<RespuestaApi<AccesoUsuario>>() {
            @Override
            public void onResponse(Call<RespuestaApi<AccesoUsuario>> call, retrofit2.Response<RespuestaApi<AccesoUsuario>> response) {
                String bearerToken;
                if (response.isSuccessful()) {
                    RespuestaApi<AccesoUsuario> token = response.body();
                    assert token != null;
                    bearerToken = token.Mensaje;
                    db.updateToken(bearerToken);
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition(0, 0);
                } else {
                    String titulo = "AVISO";
                    String mensaje = "Hubo un error al obtener el Token";
                    Modales modales = new Modales(activity);
                    View view1 = modales.MostrarDialogoAlertaAceptar(activity, mensaje, titulo);
                    view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            modales.alertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RespuestaApi<AccesoUsuario>> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void errorToken(Activity activity) {
        String titulo = "AVISO";
        String mensaje = "Token Expirado \n Intente de nuevo";
        Modales modales = new Modales(activity);
        View view1 = modales.MostrarDialogoAlertaAceptar(activity, mensaje, titulo);
        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAndUpdateToken(activity);
                modales.alertDialog.dismiss();
            }
        });
    }

    public static void errorTokenWithReload(Activity activity) {
        String titulo = "AVISO";
        String mensaje = "Token Expirado \n Intente de nuevo";
        Modales modales = new Modales(activity);
        View view1 = modales.MostrarDialogoAlertaAceptar(activity, mensaje, titulo);
        view1.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAndUpdateTokenforReload(activity);
                modales.alertDialog.dismiss();
            }
        });
    }

}


