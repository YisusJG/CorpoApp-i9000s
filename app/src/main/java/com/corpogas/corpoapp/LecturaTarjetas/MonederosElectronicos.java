package com.corpogas.corpoapp.LecturaTarjetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.device.PrinterManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.corpogas.corpoapp.R;
import com.corpogas.corpoapp.Service.MagReadService;

public class MonederosElectronicos extends AppCompatActivity {

    private EditText mNo;
    private MagReadService mReadService;
    private ToneGenerator tg = null;
    private TextView mAlertTv;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MagReadService.MESSAGE_READ_MAG:
                    //MyToast.showCrouton(MainActivity.this, "Read the card successed!", Style.CONFIRM);
                    updateAlert("Read the card successed!", 1);
                    String track1 = msg.getData().getString(MagReadService.CARD_TRACK1);
                    //mNo.setText("");
                    if(!track1.equals(""))
                        beep();
                    mNo.append(track1);
                    //mNo.append("\n\n");
                    break;
                case MagReadService.MESSAGE_OPEN_MAG:
                    //MyToast.showCrouton(MainActivity.this, "Init Mag Reader faile!", Style.ALERT);
                    updateAlert("Init Mag Reader failed!", 2);
                    break;
                case MagReadService.MESSAGE_CHECK_FAILE:
                    //MyToast.showCrouton(MainActivity.this, "Please Pay by card!", Style.ALERT);
                    updateAlert("Please Pay by card!", 2);
                    break;
                case MagReadService.MESSAGE_CHECK_OK:
                    //MyToast.showCrouton(MainActivity.this, "Pay by card OK!", Style.CONFIRM);
                    updateAlert("Pay by card successed!", 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_monederos_electronicos);

        mNo = (EditText) findViewById(R.id.editText1);
        mAlertTv = (TextView) findViewById(R.id.textView1);
        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        mReadService = new MagReadService(this, mHandler);


    }




    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mReadService.stop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mReadService.start();
    }
    private void updateAlert(String mesg, int type) {
        if(type == 2)
            mAlertTv.setBackgroundColor(Color.RED);
        else
            mAlertTv.setBackgroundColor(Color.GREEN);
        mAlertTv.setText(mesg);

    }
    private void beep() {
        if (tg != null)
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_CALLWAITING);
    }

}