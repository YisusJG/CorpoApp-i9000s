package com.corpogas.corpoapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ComprobarConexionInternet {
    /**
     * Función para comprobar si hay conexión a Internet
     * @param context
     * @return boolean
     */
    public static boolean compruebaConexion(Context context) {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }
}
