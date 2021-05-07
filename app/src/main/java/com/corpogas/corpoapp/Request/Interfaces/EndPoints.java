package com.corpogas.corpoapp.Request.Interfaces;

import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.UpdateApp.ApplicationUpdate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {

//    METODOS GET
    @GET("api/applicationUpdate/branchId/{branchId}/lastUpdates")
    Call<ApplicationUpdate> getActializaApp(@Path("branchId") String sucursalId);

    @GET("api/estaciones/ip/{octeto1}/{octeto2}/{octeto3}/{octeto4}")
    Call<Estacion> getEstacioApi(@Path("octeto1") String octeto1, @Path("octeto2") String octeto2, @Path("octeto3") String octeto3, @Path("octeto4") String octeto4);

    @GET("api/tickets/cabecero/sucursalId/{sucursalId}")
    Call<Ticket> getTicketsApi(@Path("sucursalId") long estacionId);



//   METODOS POST

    @POST("api/ConfiguracionAplicaciones/ValidarConexion")
    Call<ConfiguracionAplicacion> getConexionApi(@Body ConfiguracionAplicacion configuracionAplicacion);

}


