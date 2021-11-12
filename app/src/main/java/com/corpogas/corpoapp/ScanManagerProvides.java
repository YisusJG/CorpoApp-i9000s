package com.corpogas.corpoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.device.scanner.configuration.Triggering;

public class ScanManagerProvides {

    static final String TAG = "ScanManagerDemo"; //esta linea está en todas las clases
    static final boolean DEBUG = true;

    private android.device.ScanManager mScanManager = null; //esta está en 2 clases
    private static boolean mScanEnable = true; //esta está en 2 clases

    public void initScan(Context context) {
        mScanManager = new android.device.ScanManager();
        boolean powerOn = mScanManager.getScannerState();
        if (!powerOn) {
            powerOn = mScanManager.openScanner();
            if (!powerOn) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Scanner cannot be turned on!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
            }
        }
    }


    public Triggering getTriggerMode() {
        return mScanManager.getTriggerMode();
    }


    public boolean getlockTriggerState() {
        return mScanManager.getTriggerLockState();
    }


    public void startDecode() {
        if (!mScanEnable) {
            LogI("startDecode ignore, Scan enable:" + mScanEnable);
            return;
        }
        boolean lockState = getlockTriggerState();
        if (lockState) {
            LogI("startDecode ignore, Scan lockTrigger state:" + lockState);
            return;
        }
        if (mScanManager != null) {
            mScanManager.startDecode();
        }
    }


    public void stopDecode() {
        if (!mScanEnable) {
            LogI("stopDecode ignore, Scan enable:" + mScanEnable);
            return;
        }
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
    }


    public void LogD(String msg) {
        if (DEBUG) {
            android.util.Log.d(TAG, msg);
        }
    }

    public void LogI(String msg) {
        android.util.Log.i(TAG, msg);
    }
}
