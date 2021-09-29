package com.corpogas.corpoapp.Interfaces.Endpoints;

import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Catalogos.PaperVoucherType;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreCarrete;
import com.corpogas.corpoapp.Entities.Cortes.CierreDespachoDetalle;
import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Cortes.CierreFormaPago;
import com.corpogas.corpoapp.Entities.Cortes.LecturaManguera;
import com.corpogas.corpoapp.Entities.Estaciones.Combustible;
import com.corpogas.corpoapp.Entities.Estaciones.DiferenciaPermitida;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.EstacionControl;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.HandHeld.ListaSucursalFormaPago;
import com.corpogas.corpoapp.Entities.Sistemas.Conexion;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Sucursales.Update;
import com.corpogas.corpoapp.Entities.TanqueLleno.RespuestaIniAuto;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tarjetas.RespuestaTanqueLleno;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {

//    METODOS GET
    @GET("api/actualizaciones/sucursalId/{sucursalId}/aplicacionId/3/lastUpdates")
    Call<RespuestaApi<Update>> getActializaApp(@Path("sucursalId") String sucursalId);

    @GET("api/estaciones/ip/{octeto1}/{octeto2}/{octeto3}/{octeto4}")
    Call<Estacion> getEstacionApi(@Path("octeto1") String octeto1, @Path("octeto2") String octeto2, @Path("octeto3") String octeto3, @Path("octeto4") String octeto4);

    @GET("api/tickets/cabecero/sucursalId/{sucursalId}")
    Call<Ticket> getTicketsApi(@Path("sucursalId") long sucursalId);

    @GET("api/accesoUsuarios/sucursal/{sucursalId}/clave/{clave}")
    Call<RespuestaApi<AccesoUsuario>> getAccesoUsuario(@Path("sucursalId") long sucursalId, @Path("clave") String clave);

    @GET("api/sucursalFormaPagos/sucursal/{sucursalId}")
    Call<List<BranchPaymentMethod>> getFormaPagos(@Path("sucursalId") long sucursalId);

    @GET("api/Empleados/clave/{clave}")
    Call<RespuestaApi<Empleado>> getDatosEmpleado(@Path("clave") String clave);

    @GET("api/ventaProductos/sucursal/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<List<ProductoTarjetero>>> getProductosProcedencia(@Path("sucursalId") long sucursalId, @Path("posicionCargaId") long posicionCargaId);

    @GET("api/tickets/validaPendienteCobro/sucursalId/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<Boolean>> getTicketPendienteCobro(@Path("sucursalId") long sucursalId,@Path("posicionCargaId") long posicionCargaId);

    @GET("api/despachos/autorizaDespacho/posicionCargaId/{posicionCargaId}/usuarioId/{usuarioId}")
    Call<RespuestaApi<Boolean>> getAutorizaDespacho (@Path("posicionCargaId") long posicionCargaId, @Path("usuarioId") long usuarioId);

    @GET("api/Islas/productos/sucursal/{sucursalId}/posicionCargaId/{id}")
    Call<Isla> getPosicionCargaProductosSucursal(@Path("sucursalId") long sucursalId, @Path("id") String id);

    @GET("api/estacionCombustibles/sucursal/{sucursalId}")
    Call<RespuestaApi<List<Combustible>>>  getCombustiblesPorSucursalId(@Path("sucursalId") long sucursalId);

    @GET("api/lecturaMangueras/sucursal/{sucursalId}/lecturaMangueraPorPosicionCarga/isla/{islaId}/posicionCarga/{posicionId}")
    Call<List<LecturaManguera>> getLecturaMangueraPorPosicionCargaIdLecturaMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Path("posicionId") long posicionId);

    @GET("api/diferenciapermitidas/sucursalId/{sucursalId}")
    Call<RespuestaApi<DiferenciaPermitida>> getDiferenciaPermitidaPorSucursalId(@Path("sucursalId") long sucursalId);

    @GET("api/estacioncontroles/sucursal/{sucursalId}/ClaveEmpleado/{claveEmpleado}")
    Call<List<EstacionControl>> getPorSucursalClaveEmpleado(@Path("sucursalId") long sucursalId, @Path("claveEmpleado") String claveEmpleado);

    @GET("api/cierreCarretes/sucursal/{sucursalId}/isla/{islaId}")
    Call<RespuestaApi<List<CierreCarrete>>> getLecturaInicialMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId);

    @GET("api/cierreDespachoDetalles/sucursalId/{sucursalId}/islaId/{islaId}")
    Call<RespuestaApi<List<CierreDespachoDetalle>>> getDespachoDetalle(@Path("sucursalId") long sucursalId,@Path("islaId") long islaId);

    @GET("api/cierreFormaPagos/sucursal/{sucursalId}/isla/{islaId}")
    Call<RespuestaApi<List<CierreFormaPago>>> getFormaPagosUltimoTurno(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId);

    @GET("api/TipoValePapeles")
    Call<PaperVoucherType> getTipoValePapel();

    // METODO PARA VALIDAR NIP DE USUARIO

    @GET("api/asignacionDispositivos/validaNip/{nip}/dispositivoId/{dispositivoId}")
    Call<RespuestaApi<Empleado>> getValidaNip(@Path("nip") int nip, @Path("dispositivoId") long dispositivoId);



//   METODOS POST

    @POST("api/ConfiguracionAplicaciones/ValidarConexion")
    Call<ConfiguracionAplicacion> getConexionApi(@Body ConfiguracionAplicacion configuracionAplicacion);

    @POST("api/bines/obtieneBinTarjeta/sucursalId/{sucursalId}")
    Call<RespuestaApi<Bin>> getBin(@Path("sucursalId") long sucursalId, @Body Bin bin);

    @POST("api/puntadas/actualizaPuntos/clave/{clave}")
    Call<RespuestaApi<Puntada>> getActualizaPuntos(@Path("clave") String clave, @Body Puntada puntada);

    @POST("api/tickets/generar")
    Call<Ticket> getGenerarTicket(@Body TicketRequest ticketRequest);

    @POST("api/Transacciones/finalizaVenta/sucursal/{sucursalId}/posicionCarga/{posicionCarga}/usuario/{usuarioId}")
    Call<RespuestaApi<Transaccion>> getPostFinalizaVenta(@Path("sucursalId") long sucursalId, @Path("posicionCarga") long posicionCarga, @Path("usuarioId") long usuarioId);

    @POST("api/cierreCarretes/sucursal/{sucursalId}/isla/{islaId}/usuario/{usuarioId}")
    Call<RespuestaApi<List<CierreCarrete>>> postLecturaInicialMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Path("usuarioId") long usuarioId);

    @POST("api/cierres/cabecero/sucursal/{sucursalId}/isla/{islaId}/usuario/{usuarioId}")
    Call<RespuestaApi<Cierre>> getCrearCierre(@Path("sucursalId") long sucursalId,@Path("islaId") long islaId,@Path("usuarioId") long usuarioId);

    @POST("api/cierreFajillas/fajillas/usuario/{usuarioId}")
    Call<RespuestaApi<List<CierreFajilla>>> postGuardaFoliosCierreListaFajillas(@Body List<CierreFajilla> cierreFajillas, @Path("usuarioId") long usuarioId);

    @POST("api/tanqueLleno/InicioAutorizacion/clave/{clave}")
    Call<RespuestaTanqueLleno> getInicializaAuto(@Path("clave") String clave, @Body RespuestaIniAuto respuestaIniAuto);

    @POST("api/tanqueLleno/EnviarProductos")
//    Call<RespuestaEnviarProductos> getEnviaProductos(@Body RespuestaEnviarProductos respuestaEnviarProductos);




//    METODOS DELETE

    @DELETE("api/ventaProductos/BorraProductos/sucursal/{sucursalId}/usuario/{usuarioId}/posicionCarga/{posicionCargaId}")
    Call<ResponseBody> deleteProductos(@Path("sucursalId") long sucursalId,@Path("usuarioId") long usuarioId,@Path("posicionCargaId") long posicionCargaId);
}


