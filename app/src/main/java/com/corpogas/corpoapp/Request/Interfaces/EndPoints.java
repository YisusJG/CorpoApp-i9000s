package com.corpogas.corpoapp.Request.Interfaces;

import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.HandHeld.ListaSucursalFormaPago;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Sucursales.Update;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {

//    METODOS GET
    @GET("api/actualizaciones/sucursalId/{sucursalId}/lastUpdates")
    Call<Update> getActializaApp(@Path("sucursalId") String sucursalId);

    @GET("api/estaciones/ip/{octeto1}/{octeto2}/{octeto3}/{octeto4}")
    Call<Estacion> getEstacioApi(@Path("octeto1") String octeto1, @Path("octeto2") String octeto2, @Path("octeto3") String octeto3, @Path("octeto4") String octeto4);

    @GET("api/tickets/cabecero/sucursalId/{sucursalId}")
    Call<Ticket> getTicketsApi(@Path("sucursalId") long estacionId);

    @GET("api/accesoUsuarios/sucursal/{sucursalId}/clave/{clave}")
    Call<RespuestaApi<AccesoUsuario>> getAccesoUsuario(@Path("sucursalId") String sucursalId, @Path("clave") String clave);

    @GET("api/sucursalFormaPagos/sucursal/{sucursalId}")
    Call<List<BranchPaymentMethod>> getFormaPagos(@Path("sucursalId") String sucursalId);

    @GET("api/Empleados/clave/{clave}")
    Call<RespuestaApi<Empleado>> getDatosEmpleado(@Path("clave") String clave);

    @GET("api/ventaProductos/sucursal/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<List<ProductoTarjetero>>> getProductosProcedencia(@Path("sucursalId") String sucursalId, @Path("posicionCargaId") long posicionCargaId);

    @GET("api/tickets/validaPendienteCobro/sucursalId/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<Boolean>> getTicketPendienteCobro(@Path("sucursalId") String sucursalId,@Path("posicionCargaId") long posicionCargaId);

    @GET("api/despachos/autorizaDespacho/posicionCargaId/{posicionCargaId}/usuarioId/{usuarioId}")
    Call<RespuestaApi<Boolean>> getAutorizaDespacho (@Path("posicionCargaId") long posicionCargaId, @Path("usuarioId") String usuarioId);


//   METODOS POST

    @POST("api/ConfiguracionAplicaciones/ValidarConexion")
    Call<ConfiguracionAplicacion> getConexionApi(@Body ConfiguracionAplicacion configuracionAplicacion);

    @POST("api/bines/obtieneBinTarjeta/sucursalId/{sucursalId}")
    Call<RespuestaApi<Bin>> getBin(@Path("sucursalId") String sucursalId, @Body Bin bin);

    @POST("api/puntadas/actualizaPuntos/clave/{clave}")
    Call<RespuestaApi<Puntada>> getActualizaPuntos(@Path("clave") String clave, @Body Puntada puntada);

    @POST("api/tickets/generar")
    Call<Ticket<TicketRequest>> getGenerarTicket(@Body TicketRequest ticketRequest);

    @POST("api/Transacciones/finalizaVenta/sucursal/{sucursalId}/posicionCarga/{posicionCarga}/usuario/{usuarioId}")
    Call<RespuestaApi<Transaccion>> getPostFinalizaVenta(@Path("sucursalId") String sucursalId, @Path("posicionCarga") long posicionCarga, @Path("usuarioId") String usuarioId);

}


