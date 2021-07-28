package com.corpogas.corpoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.device.PrinterManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.corpogas.corpoapp.Service.PrintBillService;

public class MainActivity extends AppCompatActivity {
    Button btnImprimir;
    private PrinterManager printer = new PrinterManager();
    private final static String STR_PRNT_SALE = "sale";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnImprimir = findViewById(R.id.btnImprimirPruebas);
        btnImprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messgae ="Pruebas yisus";
//                mBtnPrnBill.setEnabled(false);
                int ret = printer.prn_getStatus();
                if (ret == 0) {
                    doprintwork(STR_PRNT_SALE);
//                        doprintwork("Sales un yisus");// print sale

                } else {
                    doprintwork(STR_PRNT_SALE);
//                    Intent intent = new Intent(PRNT_ACTION);
//                    intent.putExtra("ret", ret);
//                    sendBroadcast(intent);
                }

            }
        });
    }
    void doprintwork(String msg) {

//        printer.prn_open();
//        printer.prn_setupPage(_XVALUE, -1);
//        printer.prn_clearPage();
//        printer.prn_drawText(("打印机测试"), 70, 50, (STR_FONT_VALUE_SONG), 48 , false, false, 0);
//        height += 50;
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        opts.inDensity = getResources().getDisplayMetrics().densityDpi;
//        opts.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unionpay_logo, opts);
//        printer.prn_drawBitmap(bitmap, 84, height);
//        height += 80;
//        Prn_Str("商户名称：测试商户", _YVALUE6, height);

        Intent intentService = new Intent(this, PrintBillService.class);
        intentService.putExtra("SPRT", msg);
        startService(intentService);
    }
}