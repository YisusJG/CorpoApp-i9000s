package com.corpogas.corpoapp.Modales;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.corpogas.corpoapp.R;


public class Modales extends Dialog implements
        View.OnClickListener {

    public AlertDialog alertDialog;

    public Modales(Activity a) {
        super(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }

    public View MostrarDialogoCorrecto(Context context, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_satisfactorio,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(R.string.titulo_correcto);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonAction)).setText(R.string.aceptar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;


    }

    public View MostrarDialogoCorrecto(Context context,String titulo,String mensaje,String nombreAceptar){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_satisfactorio,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonAction)).setText(nombreAceptar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;


    }


    //SE crao un modal con sobrecarga temporal esto hau que cambiarlo en todos despues para que sea dinamio los mensajes, nombres de boton y leyenda


    public View MostrarDialogoCorrectoOpciones(Context context,String titulo, String mensaje,String nombrebtnAceptar, String nombreBtnCancelar){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_satisfactorio_opciones,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonYes)).setText(nombrebtnAceptar);
        ((Button) view.findViewById(R.id.buttonNo)).setText(nombreBtnCancelar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_success);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;


    }

    public View MostrarDialogoAlerta(Context context,String mensaje,String nombrebtnAceptar, String nombreBtnCancelar){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_alerta,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(R.string.titulo_aviso);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonYes)).setText(nombrebtnAceptar);
        ((Button) view.findViewById(R.id.buttonNo)).setText(nombreBtnCancelar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        alertDialog = builder.create();
        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }

    public View MostrarDialogoAlertaAceptar(Context context,String mensaje,String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_alerta_aceptar,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonYes)).setText(R.string.aceptar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        alertDialog = builder.create();


        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }

    public View MostrarDialogoInsertaDato(Context context, String mensaje, String titulo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_insertar_dato_dialogo, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
//        ((EditText) view.findViewById(R.id.textInsertarDato)).getText();
        ((Button) view.findViewById(R.id.buttonYes)).setText(R.string.aceptar);
        ((Button) view.findViewById(R.id.buttonNo)).setText(R.string.cancelar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_agregar_dato);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }



    public View MostrarDialogoError(Context context, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_error,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(R.string.titulo_error);
        ((TextView) view.findViewById(R.id.textMessage)).setText(mensaje);
        ((Button) view.findViewById(R.id.buttonAction)).setText(R.string.aceptar);
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_error);

        alertDialog = builder.create();
        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;
    }

    public View MostrarDialogoInsertaFactura(Context context, String tituloHeader) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_insertar_facturas_dialogo, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(tituloHeader);
        ((Button) view.findViewById(R.id.btnEnviarFactura)).setText("Enviar");
        ((Button) view.findViewById(R.id.btncancelarFactura)).setText(R.string.cancelar);
        ((Button) view.findViewById(R.id.btnImprimirFactura)).setText("Imprimir");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_agregar_dato);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }

    public View MostrarDialogoEfectivoMi(Context context, String monto, String recibi, String cambio, String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_pago_efectivo, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleEfectivo)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMonto)).setText(monto);
        ((TextView) view.findViewById(R.id.textMontoVales)).setText(recibi);
        ((TextView) view.findViewById(R.id.textCambio)).setText(cambio);
        ((Button) view.findViewById(R.id.btnAceptarVales)).setText("ACEPTAR");
        ((Button) view.findViewById(R.id.btnCancelarVales)).setText("CANCELAR");

        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null)
        {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }


    public View MostrarDialogoEfectivo(Context context, String monto, String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_dialogo_pago_efectivo, (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitleEfectivo)).setText(titulo);
        ((TextView) view.findViewById(R.id.textMonto)).setText(monto);
        //((TextView) view.findViewById(R.id.textMontoVales)).setText(recibi);
        EditText cantidadRecibida = view.findViewById(R.id.textMonto);
        //String montoRecibido = cantidadRecibida.getText().toString();
        Double vuelto = Double.parseDouble(cantidadRecibida.getText().toString()) - Double.parseDouble(monto);
        ((TextView) view.findViewById(R.id.textCambio)).setText(vuelto.toString());
        ((Button) view.findViewById(R.id.btnAceptarVales)).setText("ACEPTAR");
        ((Button) view.findViewById(R.id.btnCancelarVales)).setText("CANCELAR");

        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_warning);

        alertDialog = builder.create();

        if(alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.setCancelable(false);
        alertDialog.show();

        return view;

    }





    @Override
    public void onClick(View v) {

    }
}