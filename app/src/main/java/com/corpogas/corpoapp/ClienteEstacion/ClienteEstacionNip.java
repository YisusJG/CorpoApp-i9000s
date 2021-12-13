package com.corpogas.corpoapp.ClienteEstacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.PosicionCargaTLl;
import com.corpogas.corpoapp.TanqueLleno.md5;

import java.math.BigInteger;

public class ClienteEstacionNip extends AppCompatActivity {

    EditText edtNipClienteEstacion;
    String password, nipMd5Cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_estacion_nip);

//        getToken();
        init();
        getObjetos();
        setVariables();

        onClicks();
    }

    private void init(){

        edtNipClienteEstacion = (EditText) findViewById(R.id.edtNipClienteEstacion);

    }

    private void getObjetos(){

    }

    private void setVariables(){

    }

    private void onClicks(){
//        edtNipClienteEstacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                edtNipClienteEstacion.setText("");
//            }
//        });

    }

    private void calculos() {
        //Se lee el password del objeto y se asigna a variable

        password = edtNipClienteEstacion.getText().toString();
        if (password.isEmpty()) {
            edtNipClienteEstacion.setError("Ingrese el NIP de la Tarjeta");
            return;
        }
        if (password.equals("1234")){
            Intent intent5 = new Intent(getApplicationContext(), PosicionCargaTLl.class);
            startActivity(intent5);
            finish();
            return;
        }
        if (password != "1234"){
            edtNipClienteEstacion.setText("");
            edtNipClienteEstacion.setError("El NIP que ingresaste es incorrecto");
            return;
        }

//        else {
//
//            obtieneNipMd5();
//
//
//
//
//            Intent intent5 = new Intent(getApplicationContext(), PosicionCargaTLl.class);
//
//            startActivity(intent5);
//            finish();
//
//        }
    }

    private void obtieneNipMd5(){

        byte [] md5Input = password.getBytes();
        BigInteger md5Data = null;

        try{
            md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        nipMd5Cliente = md5Data.toString(16);

    }

    //procedimiento para  cachar el Enter del teclado
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                calculos();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}