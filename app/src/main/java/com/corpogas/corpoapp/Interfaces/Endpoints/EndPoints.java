package com.corpogas.corpoapp.Interfaces.Endpoints;

import com.corpogas.corpoapp.Cofre.Entities.EnviarFajillaCofre;
import com.corpogas.corpoapp.Cofre.Entities.RecepcionFajillasNoEnCajaFuerte;
import com.corpogas.corpoapp.Cofre.Entities.StatusFajilla;
import com.corpogas.corpoapp.Cofre.Entities.TotalFajillaCajaFuerte;
import com.corpogas.corpoapp.Entities.Accesos.AccesoUsuario;
import com.corpogas.corpoapp.Entities.Catalogos.Bin;
import com.corpogas.corpoapp.Entities.Classes.RespuestaApi;
import com.corpogas.corpoapp.Entities.Common.EstadoPosicion;
import com.corpogas.corpoapp.Entities.Common.ProductoTarjetero;
import com.corpogas.corpoapp.Entities.Cortes.Cierre;
import com.corpogas.corpoapp.Entities.Cortes.CierreCarrete;
import com.corpogas.corpoapp.Entities.Cortes.CierreDespachoDetalle;
import com.corpogas.corpoapp.Entities.Cortes.CierreFajilla;
import com.corpogas.corpoapp.Entities.Cortes.CierreFormaPago;
import com.corpogas.corpoapp.Entities.Cortes.LecturaManguera;
import com.corpogas.corpoapp.Entities.Estaciones.BodegaProducto;
import com.corpogas.corpoapp.Entities.Estaciones.Combustible;
import com.corpogas.corpoapp.Entities.Estaciones.DiferenciaPermitida;
import com.corpogas.corpoapp.Entities.Estaciones.Empleado;
import com.corpogas.corpoapp.Entities.Estaciones.EndPointYena;
import com.corpogas.corpoapp.Entities.Estaciones.Estacion;
import com.corpogas.corpoapp.Entities.Estaciones.EstacionControl;
import com.corpogas.corpoapp.Entities.Estaciones.Isla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajilla;
import com.corpogas.corpoapp.Entities.Estaciones.RecepcionFajillaEntregada;
import com.corpogas.corpoapp.Entities.Estaciones.ResumenFajilla;
import com.corpogas.corpoapp.Entities.Sistemas.ConfiguracionAplicacion;
import com.corpogas.corpoapp.Entities.Sucursales.BranchPaymentMethod;
import com.corpogas.corpoapp.Entities.Sucursales.Update;
import com.corpogas.corpoapp.Entities.TanqueLleno.RespuestaIniAuto;
import com.corpogas.corpoapp.Entities.Tarjetas.Puntada;
import com.corpogas.corpoapp.Entities.Tarjetas.RespuestaTanqueLleno;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketRequest;
import com.corpogas.corpoapp.Entities.Ventas.Transaccion;
import com.corpogas.corpoapp.Entities.Virtuales.Arqueo;
import com.corpogas.corpoapp.Entities.Virtuales.CierreTicket;
import com.corpogas.corpoapp.Entities.Virtuales.CierreVariables;
import com.corpogas.corpoapp.Entities.Yena.YenaResponse;
import com.corpogas.corpoapp.Entregas.Entities.PaperVoucherType;
import com.corpogas.corpoapp.Entregas.Entities.RecepcionVale;
import com.corpogas.corpoapp.Entregas.Entities.ResumenVale;
import com.corpogas.corpoapp.Facturacion.Entities.PeticionRFC;
import com.corpogas.corpoapp.Facturacion.Entities.RespuestaRFC;
import com.corpogas.corpoapp.Facturacion.Entities.RespuestaSolicitudFactura;
import com.corpogas.corpoapp.Facturacion.Entities.SolicitudFactura;
import com.corpogas.corpoapp.Metas.Entities.Metas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface EndPoints {

//    METODOS GET
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/actualizaciones/sucursalId/{sucursalId}/aplicacionId/3/lastUpdates")
    Call<RespuestaApi<Update>> getActializaApp(@Path("sucursalId") String sucursalId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/estaciones/ip/{octeto1}/{octeto2}/{octeto3}/{octeto4}")
    Call<Estacion> getEstacionApi(@Path("octeto1") String octeto1, @Path("octeto2") String octeto2, @Path("octeto3") String octeto3, @Path("octeto4") String octeto4, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/tickets/cabecero/sucursalId/{sucursalId}")
    Call<Ticket> getTicketsApi(@Path("sucursalId") long sucursalId, @Header("Authorization") String auth);

    @GET("api/accesoUsuarios/sucursal/{sucursalId}/clave/{clave}")
    Call<RespuestaApi<AccesoUsuario>> getAccesoUsuario(@Path("sucursalId") long sucursalId, @Path("clave") String clave);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/accesoUsuarios/sucursal/{sucursalId}/NumeroEmpleado/{numeroempleado}")
    Call<RespuestaApi<AccesoUsuario>> getAccesoUsuarionumeroempleado(@Path("sucursalId") long sucursalId, @Path("numeroempleado") String numeroempleado, @Header("Authorization") String auth);

    //AGERGADO MIKEL
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/accesoUsuarios/sucursal/{sucursalId}")
    Call<RespuestaApi<AccesoUsuario>> getAccesoUsuariosucursal(@Path("sucursalId") long sucursalId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/sucursalFormaPagos/sucursal/{sucursalId}")
    Call<List<BranchPaymentMethod>> getFormaPagos(@Path("sucursalId") long sucursalId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/Empleados/clave/{clave}")
    Call<RespuestaApi<Empleado>> getDatosEmpleado(@Path("clave") String clave, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/ventaProductos/sucursal/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<List<ProductoTarjetero>>> getProductosProcedencia(@Path("sucursalId") long sucursalId, @Path("posicionCargaId") long posicionCargaId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/tickets/validaPendienteCobro/sucursalId/{sucursalId}/posicionCargaId/{posicionCargaId}")
    Call<RespuestaApi<Boolean>> getTicketPendienteCobro(@Path("sucursalId") long sucursalId,@Path("posicionCargaId") long posicionCargaId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/despachos/autorizaDespacho/posicionCargaId/{posicionCargaId}/usuarioId/{usuarioId}")
    Call<RespuestaApi<Boolean>> getAutorizaDespacho (@Path("posicionCargaId") long posicionCargaId, @Path("usuarioId") long usuarioId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/Islas/productos/sucursal/{sucursalId}/posicionCargaId/{id}")
    Call<Isla> getPosicionCargaProductosSucursal(@Path("sucursalId") long sucursalId, @Path("id") String id, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/estacionCombustibles/sucursal/{sucursalId}")
    Call<RespuestaApi<List<Combustible>>>  getCombustiblesPorSucursalId(@Path("sucursalId") long sucursalId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/lecturaMangueras/sucursal/{sucursalId}/lecturaMangueraPorPosicionCarga/isla/{islaId}/posicionCarga/{posicionId}")
    Call<List<LecturaManguera>> getLecturaMangueraPorPosicionCargaIdLecturaMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Path("posicionId") long posicionId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/diferenciapermitidas/sucursalId/{sucursalId}")
    Call<RespuestaApi<DiferenciaPermitida>> getDiferenciaPermitidaPorSucursalId(@Path("sucursalId") long sucursalId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/estacioncontroles/sucursal/{sucursalId}/ClaveEmpleado/{claveEmpleado}")
    Call<List<EstacionControl>> getPorSucursalClaveEmpleado(@Path("sucursalId") long sucursalId, @Path("claveEmpleado") String claveEmpleado, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/cierreCarretes/sucursal/{sucursalId}/isla/{islaId}")
    Call<RespuestaApi<List<CierreCarrete>>> getLecturaInicialMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/cierreDespachoDetalles/sucursalId/{sucursalId}/islaId/{islaId}")
    Call<RespuestaApi<List<CierreDespachoDetalle>>> getDespachoDetalle(@Path("sucursalId") long sucursalId,@Path("islaId") long islaId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/cierreFormaPagos/sucursal/{sucursalId}/isla/{islaId}")
    Call<RespuestaApi<List<CierreFormaPago>>> getFormaPagosUltimoTurno(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Header("Authorization") String auth);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/TipoValePapeles")
    Call<List<PaperVoucherType>> getTipoValePapel(@Header("Authorization") String authHeader);

    // METODO PARA VALIDAR NIP DE USUARIO

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/asignacionDispositivos/validaNip/{nip}/dispositivoId/{dispositivoId}")
    Call<RespuestaApi<Empleado>> getValidaNip(@Path("nip") int nip, @Path("dispositivoId") long dispositivoId,  @Header("Authorization") String authHeader); // para token
//    Call<RespuestaApi<Empleado>> getValidaNip(@Path("nip") int nip, @Path("dispositivoId") long dispositivoId);  antes

    //Metodo para obtener Precio fajillas y Denominaciones de Billetes

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/cierreVariables/sucursalId/{sucursalId}")
    Call<RespuestaApi<CierreVariables>> getCierreVariables(@Path("sucursalId") long sucursalId, @Header("Authorization") String authHeader);

    // METODO PARA OBTENER LA VENTA DESGLOSADA DEL DESPACHADOR

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/arqueos/sucursal/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<List<Arqueo>>> getArqueo(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado, @Header("Authorization") String authHeader);

    // METODO PARA OBTENER EL RESUMEN DEL CORTE POR USUARIO

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/cierreTickets/turno/sucursal/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<CierreTicket>> getCierreTicket(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/recepcionFajillas/ObtenerFajillasPendientesCajaFuerte/sucursalId/{sucursalId}/numeroEmpleado/{numeroEmpleado}/qr/{qrCajaFuerte}")
    Call<RespuestaApi<RecepcionFajillasNoEnCajaFuerte<TotalFajillaCajaFuerte>>> getFajillasCofre(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado, @Path ("qrCajaFuerte") String qrCajaFuerte, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/recepcionFajillas/getFajillas/sucursalId/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<RecepcionFajillaEntregada>> getFajillas(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") long numeroEmpleado, @Header("Authorization") String authHeader);

    //METOTO PARA OBTENER EL MAXIMO DE EFECTIVO QUE PUEDE TENER UN DESPACHADOR POR ESTACION
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/Consolas/sucursalId/{sucursalId}/maximoEfectivo")
    Call<RespuestaApi<Double>> getMaximoEfectivo(@Path("sucursalId") long sucursalId, @Header("Authorization") String authHeader);

    //METODO PARA OBTENER EL EFECTIVO NO ENTREGADO
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/recepcionFajillas/efectivoNoEntregado/sucursalId/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<Double>> getEfectivoNoEntregado(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/objetivoVentas/sucursal/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<List<Metas>>> getMetas(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado, @Header("Authorization") String authHeader);

    @GET("api/BodegaProductos/obtieneExistenciaPorEmpleado/sucursalId/{sucursalId}/numeroEmpleado/{numeroEmpleado}")
    Call<RespuestaApi<List<BodegaProducto>>> getExistenciaPorEmpleado(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado);

    // METODO PARA OBTENER POSICIONES DE CARGA POR EMPLEADO
    @GET("api/posicionCargas/GetPosicionCargaEmpleadoId/sucursal/{sucursalId}/empleado/{numeroEmpleado}")
    Call<RespuestaApi<List<EstadoPosicion>>> obtenerPosicionCargaEmpleadoId(@Path("sucursalId") long sucursalId, @Path("numeroEmpleado") String numeroEmpleado);

    // METODO PARA OBTENER POSICIONES DE CARGA POR SUCURSAL
    @GET("api/posicionCargas/GetPosicionCargasEstacion/sucursal/{sucursalId}")
    Call<RespuestaApi<List<EstadoPosicion>>> obtenerPosicionCargasEstacion(@Path("sucursalId") long sucursalId);


//   METODOS POST

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/ConfiguracionAplicaciones/ValidarConexion")
    Call<ConfiguracionAplicacion> getConexionApi(@Body ConfiguracionAplicacion configuracionAplicacion, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/bines/obtieneBinTarjeta/sucursalId/{sucursalId}")
    Call<RespuestaApi<Bin>> getBin(@Path("sucursalId") long sucursalId, @Body Bin bin, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/puntadas/actualizaPuntos/clave/{clave}")
    Call<RespuestaApi<Puntada>> getActualizaPuntos(@Path("clave") String clave, @Body Puntada puntada, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/tickets/generar")
    Call<Ticket> getGenerarTicket(@Body TicketRequest ticketRequest, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/Transacciones/finalizaVenta/sucursal/{sucursalId}/posicionCarga/{posicionCarga}/usuario/{usuarioId}")
    Call<RespuestaApi<Transaccion>> getPostFinalizaVenta(@Path("sucursalId") long sucursalId, @Path("posicionCarga") long posicionCarga, @Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/cierreCarretes/sucursal/{sucursalId}/isla/{islaId}/usuario/{usuarioId}")
    Call<RespuestaApi<List<CierreCarrete>>> postLecturaInicialMecanica(@Path("sucursalId") long sucursalId, @Path("islaId") long islaId, @Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/cierres/cabecero/sucursal/{sucursalId}/isla/{islaId}/usuario/{usuarioId}")
    Call<RespuestaApi<Cierre>> getCrearCierre(@Path("sucursalId") long sucursalId,@Path("islaId") long islaId,@Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/cierreFajillas/fajillas/usuario/{usuarioId}")
    Call<RespuestaApi<List<CierreFajilla>>> postGuardaFoliosCierreListaFajillas(@Body List<CierreFajilla> cierreFajillas, @Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/tanqueLleno/InicioAutorizacion/clave/{clave}")
    Call<RespuestaTanqueLleno> getInicializaAuto(@Path("clave") String clave, @Body RespuestaIniAuto respuestaIniAuto, @Header("Authorization") String authHeader);

//    @POST("api/tanqueLleno/EnviarProductos")
//    Call<RespuestaEnviarProductos> getEnviaProductos(@Body RespuestaEnviarProductos respuestaEnviarProductos);

    //Metodo para enviar Picos Billetes
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/cierreFajillas/picoBillete/usuario/{usuarioId}") //Metodo para enviar Picos Billetes
    Call<RespuestaApi<List<CierreFajilla>>> postGuardaPicoBilletes(@Body List<CierreFajilla> cierreFajillas, @Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    //Metodo para enviar Picos Morralla
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/cierreFajillas/usuario/{usuarioId}")
    Call<RespuestaApi<CierreFajilla>> postGuardaFoliosCierreFajillas(@Body CierreFajilla cierreFajilla, @Path("usuarioId") long usuarioId, @Header("Authorization") String authHeader);

    // METODOS PARA SOLICITUD DE FACTURAS

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/facturas/Rfc")
    Call<RespuestaApi<List<RespuestaRFC>>> postObtenerRfcs(@Body PeticionRFC peticionRFC, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/facturas/sucursalId/{sucursalId}/usuarioId/{usuarioId}/Factura")
    Call<RespuestaApi<RespuestaSolicitudFactura>> postSolicitarFactura(@Path("sucursalId") long sucursalId, @Path("usuarioId") long usuarioId, @Body SolicitudFactura solicitudFactura, @Header("Authorization") String authHeader);

    //Metodo para guardar picos billetes y picos monedas
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/recepcionFajillas/guardaFajillas/numeroEmpleadoEntrega/{numeroEmpleadoEntrega}/numeroEmpleadoAutoriza/{numeroEmpleadoAutoriza}")
    Call<RespuestaApi<List<ResumenFajilla>>> postGuardaFajillas(@Body List<RecepcionFajilla> recepcionFajilla, @Path("numeroEmpleadoEntrega") String numeroEmpleadoEntrega, @Path("numeroEmpleadoAutoriza") String numeroEmpleadoAutoriza, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/recepcionFajillas/guardaFajillasCorte/numeroEmpleadoEntrega/{numeroEmpleadoEntrega}/numeroEmpleadoAutoriza/{numeroEmpleadoAutoriza}")
    Call<RespuestaApi<List<ResumenFajilla>>> postGuardaFajillasCorte(@Body List<RecepcionFajilla> recepcionFajilla, @Path("numeroEmpleadoEntrega") String numeroEmpleadoEntrega, @Path("numeroEmpleadoAutoriza") String numeroEmpleadoAutoriza, @Header("Authorization") String authHeader);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/recepcionVales/guardaVale/numeroEmpleadoEntrega/{numeroEmpleadoEntrega}")
    Call<RespuestaApi<List<ResumenVale>>> postGuardaVales(@Body RecepcionVale recepcionVale,@Path("numeroEmpleadoEntrega") String numeroEmpleadoEntrega, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/recepcionVales/guardaVale/numeroEmpleadoEntrega/{numeroEmpleadoEntrega}/corte")
    Call<RespuestaApi<List<ResumenVale>>> postGuardaValesCorte(@Body RecepcionVale recepcionVale,@Path("numeroEmpleadoEntrega") String numeroEmpleadoEntrega, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("api/fajillasCajaFuerte/entregaFajillasCajaFuerte/numeroEmpleado/{numeroEmpleadoEntrega}")
    Call<RespuestaApi<List<StatusFajilla>>> postGuardaFajillasCofre(@Body List<EnviarFajillaCofre> enviarfajillasCofre, @Path("numeroEmpleadoEntrega") String numeroEmpleadoEntrega, @Header("Authorization") String authHeader);

//    @POST("api/fajillasCajaFuerte/entregaFajillasCajaFuerte/numeroEmpleado/{numeroEmpleadoSale}/numeroEmpleadoAutoriza/{numeroEmpleadoAutoriza}")
//    Call<RespuestaApi<List<StatusFajilla>>> postListaFajillas(@Body JSONArray arrayList, @Path("numeroEmpleadoSale") String numeroEmpleadoEntrega, @Path("numeroEmpleadoAutoriza") String numeroEmpleadoAutoriza);
//
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Api/yenas/consultaSaldo")
    Call<RespuestaApi<YenaResponse>> postConsultaYena(@Body EndPointYena endPointYena, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Api/yenas/acumulaPuntos")
    Call<RespuestaApi<Boolean>> postAcumulaPuntos(@Body EndPointYena endPointYena, @Header("Authorization") String authHeader);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Api/yenas/redimePuntos")
    Call<RespuestaApi<Boolean>> postRedimePuntos(@Body EndPointYena endPointYena, @Header("Authorization") String authHeader);


//    METODOS DELETE

    @DELETE("api/ventaProductos/BorraProductos/sucursal/{sucursalId}/usuario/{usuarioId}/posicionCarga/{posicionCargaId}")
    Call<ResponseBody> deleteProductos(@Path("sucursalId") long sucursalId,@Path("usuarioId") long usuarioId,@Path("posicionCargaId") long posicionCargaId);
}


