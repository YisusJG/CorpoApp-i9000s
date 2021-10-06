package com.corpogas.corpoapp.TanqueLleno.NFC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.device.PiccManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.TanqueLleno.TanqueLlenoNip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TarjetaNFC extends AppCompatActivity {
    private static final String TAG = "PiccCheck";

    private static final int MSG_BLOCK_NO_NONE = 0;
    private static final int MSG_BLOCK_NO_ILLEGAL = 1;
    private static final int MSG_AUTHEN_FAIL = 2;
    private static final int MSG_WRITE_SUCCESS = 3;
    private static final int MSG_WRITE_FAIL = 4;
    private static final int MSG_READ_FAIL = 5;
    private static final int MSG_SHOW_BLOCK_DATA = 6;
    private static final int MSG_ACTIVE_FAIL = 7;
    private static final int MSG_APDU_FAIL = 8;
    private static final int MSG_SHOW_APDU = 9;
    private static final int MSG_BLOCK_DATA_NONE = 10;
    private static final int MSG_AUTHEN_BEFORE = 11;
    private static final int MSG_FOUND_UID = 12;
    private Button bOpen;
    private Button bCheck;
    private PiccManager piccReader;
    private Handler handler;
    private TextView tvApdu;
    ImageView imgNFC;
    private ExecutorService exec;

    boolean hasAuthen = false;
    int blkNo;
    int scanCard = -1;
    int SNLen = -1;

    byte EMV_APDU[] = {
            0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x32, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53,
            0x2E, 0x44, 0x44, 0x46, 0x30, 0x31, 0x00
    };

    byte keyBuf[] = {
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff
    };

    byte Wbuf[] = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d,
            0x0e, 0x0f
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta_nfc);
        tvApdu = (TextView) findViewById(R.id.rev_data);
        bCheck = (Button) findViewById(R.id.picc_check);
        imgNFC = (ImageView) findViewById(R.id.imgNFC);

        bCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exec.execute(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        byte CardType[] = new byte[2];
                        byte Atq[] = new byte[14];
                        char SAK = 1;
                        byte sak[] = new byte[1];
                        sak[0] = (byte) SAK;
                        byte SN[] = new byte[10];
                        scanCard = piccReader.request(CardType, Atq);
                        if (scanCard > 0) {
                            SNLen = piccReader.antisel(SN, sak);
                            Log.d(TAG, "SNLen = " + SNLen);
                            Message msg = handler.obtainMessage(MSG_FOUND_UID);
                            msg.obj = bytesToHexString(SN, SNLen);
                            handler.sendMessage(msg);
                        }
                    }
                }, "picc check"));

            }
        });

        piccReader = new PiccManager();
        exec = Executors.newSingleThreadExecutor();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case MSG_BLOCK_NO_NONE:
                        break;
                    case MSG_BLOCK_NO_ILLEGAL:
                        break;
                    case MSG_AUTHEN_FAIL:
                        break;
                    case MSG_AUTHEN_BEFORE:
                        break;
                    case MSG_WRITE_SUCCESS:
                        break;
                    case MSG_WRITE_FAIL:
                        break;
                    case MSG_READ_FAIL:
                        break;
                    case MSG_APDU_FAIL:
                        break;
                    case MSG_BLOCK_DATA_NONE:
                        break;
                    case MSG_SHOW_BLOCK_DATA:
////                        SoundTool.getMySound(MainActivity.this).playMusic("success");
//                        String data = (String) msg.obj;
//                        tvApdu.append("\n" + data);
                        break;
                    case MSG_ACTIVE_FAIL:
                        break;
                    case MSG_SHOW_APDU:
                        break;
                    case MSG_FOUND_UID:
                        String uid = (String) msg.obj;
                        tvApdu.append("\nUID:" + uid);
//                        SoundTool.getMySound(MainActivity.this).playMusic("success");
                        Intent intent = new Intent(getApplicationContext(), TanqueLlenoNip.class);  //seccionTanqueLleno
                        intent.putExtra("track", uid);
                        startActivity(intent);
                        finish();


                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        AbrirConexion();
    }

    private void AbrirConexion(){
        exec.execute(new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                int ret = piccReader.open();
                if (ret == 0) {
                    tvApdu.append("\n Open success");
                } else {
                    tvApdu.append("Open failed \n");
                    return;
                }
            }
        }, "picc open"));

    }

    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        if (len <= 0) {
            return "";
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}