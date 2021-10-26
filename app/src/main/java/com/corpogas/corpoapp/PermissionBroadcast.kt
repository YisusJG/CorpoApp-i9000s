package com.corpogas.corpoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class PermissionBroadcast : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val bundle = p1!!.extras
        val permissions = bundle!!.getString("permissions")
        val operations = bundle.getString("operations")
        Toast.makeText(p0!!, permissions, Toast.LENGTH_LONG).show()
        Log.d("TEXT", operations!!)
    }
}