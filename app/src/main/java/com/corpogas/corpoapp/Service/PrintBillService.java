package com.corpogas.corpoapp.Service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.tech.NfcBarcode;
import android.os.Bundle;

import com.corpogas.corpoapp.Configuracion.SQLiteBD;
import com.corpogas.corpoapp.Entities.Catalogos.Product;
import com.corpogas.corpoapp.Entities.Tickets.Ticket;
import com.corpogas.corpoapp.Entities.Tickets.TicketFormaPago;
import com.corpogas.corpoapp.Entities.Tickets.TicketProducto;
import com.corpogas.corpoapp.Menu_Principal;
import com.google.zxing.BarcodeFormat;

import com.corpogas.corpoapp.R;

import java.io.PrintWriter;


public class PrintBillService extends IntentService {
    private Ticket respuestaTicketRequest;
    private SQLiteBD db;
    private final static String STR_PRNT_BILL = "prn_bill";
    private final static String STR_PRNT_TEXT = "text";
    private final static String STR_PRNT_BLCOK = "block";
    private final static String STR_PRNT_SALE = "sale";

    private final static String STR_FONT_VALUE_SONG = "simsun";

    private static int _XVALUE = 384;
    private static int _YVALUE = 24;
    private final int _YVALUE6 = 24;
    private final int _YVALUE7 = 22;

    private static int fontSize = 24;
    private static int fontStyle = 0x0000;
    private static String fontName = STR_FONT_VALUE_SONG;

    private PrinterManager printer;

    public PrintBillService() {
        super("bill");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        db = new SQLiteBD(getApplicationContext());
        printer = new PrinterManager();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    @TargetApi(12)
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        String context = intent.getStringExtra("SPRT");
        respuestaTicketRequest = (Ticket) intent.getSerializableExtra("ticketEfectivo");

        if(context== null || context.equals("")) return ;

        int ret;
        if(context.equals(STR_PRNT_BILL)){	// print bill
            printBill();
        }else if(context.equals(STR_PRNT_BLCOK)){
            printBlock();
        }else if(context.equals(STR_PRNT_SALE)){
            try {
                printSale(getBaseContext());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {  // print string
            // add by tao.he, for custom print
            Bundle fontInfo = intent.getBundleExtra("font-info");
            android.util.Log.v("tao.he", fontInfo.toString());

            if (fontInfo != null) {
                fontSize = fontInfo.getInt("font-size", 24);
                fontStyle = fontInfo.getInt("font-style", 0);
                fontName = fontInfo.getString("font-name", STR_FONT_VALUE_SONG);
            } else {
                fontSize = 24;
                fontStyle = 0;
                fontName = STR_FONT_VALUE_SONG;
            }
            android.util.Log.v("tao.he", "font-size:" + fontSize);
            android.util.Log.v("tao.he", "font-style:" + fontStyle);
            android.util.Log.v("tao.he", "font-name:" + fontName);

            printer.prn_setupPage(384, -1);
            ret = printer.prn_drawTextEx(context, 5, 0, 384, -1, fontName, fontSize, 0, fontStyle, 0);
            printer.prn_paperForWard(20);
//            ret +=printer.prn_drawTextEx(context, 300, ret,-1,-1, "arial", 25, 1, 0, 0);
            android.util.Log.i("debug", "ret:" + ret);
        }
        // end add
        ret=printer.prn_printPage(0);

//        Intent i = new Intent(PrinterManagerActivity.PRNT_ACTION);
//        i.putExtra("ret", ret);
//        this.sendBroadcast(i);
    }

    public void printBlock(){
        printer.prn_setupPage(_XVALUE, 248);
        /* Black block */
        printer.prn_drawLine(32, 8, 136, 8, 8);
        printer.prn_drawLine(32, 12, 136, 12, 8);
        printer.prn_drawLine(32, 18, 136, 18, 8);
        printer.prn_drawLine(32, 24, 136, 24, 8);
        printer.prn_drawLine(32, 32, 136, 32, 32);

        printer.prn_drawLine(136, 56, 240, 56, 8);
        printer.prn_drawLine(136, 62, 240, 62, 8);
        printer.prn_drawLine(136, 68, 240, 68, 8);
        printer.prn_drawLine(136, 74, 240, 74, 8);
        printer.prn_drawLine(136, 80, 240, 80, 32);

        printer.prn_drawLine(240, 104, 344, 104, 8);
        printer.prn_drawLine(240, 110, 344, 110, 8);
        printer.prn_drawLine(240, 116, 344, 116, 8);
        printer.prn_drawLine(240, 122, 344, 122, 8);
        printer.prn_drawLine(240, 128, 344, 128, 32);

        printer.prn_drawLine(136, 152, 240, 152, 8);
        printer.prn_drawLine(136, 158, 240, 158, 8);
        printer.prn_drawLine(136, 164, 240, 164, 8);
        printer.prn_drawLine(136, 170, 240, 170, 8);
        printer.prn_drawLine(136, 176, 240, 176, 32);

        printer.prn_drawLine(32, 200, 136, 200, 8);
        printer.prn_drawLine(32, 206, 136, 206, 8);
        printer.prn_drawLine(32, 212, 136, 212, 8);
        printer.prn_drawLine(32, 218, 136, 218, 8);
        printer.prn_drawLine(32, 224, 136, 224, 32);
    }

    public void printBill(){
        int height = 66;
        printer.prn_setupPage(384,780);
        //   printer.prn_drawLine(0,0,384,0,2);

        printer.prn_drawText(("  打印机测试"), 5, 50, (STR_FONT_VALUE_SONG), 48 , false, false, 0);
        height += 48;
//    	printer.prn_drawText(("商户名(MERCHANT NAME):"), 0, 100, (STR_FONT_VALUE_SONG), 24 , false, false, 0);
//    	printer.prn_drawText(("  面点王（科技园店）"), 0, 126, (STR_FONT_VALUE_SONG), 24 , false, false, 0);
//
//		printer.prn_drawText(("商户号(MERCHANT NO):"), 0, 152, (STR_FONT_VALUE_SONG), 24,
//				false, false, 0);
//
//		printer.prn_drawText(("  104440358143001"), 0, 178, (STR_FONT_VALUE_SONG), 24,
//				false, false, 0);
//		printer.prn_drawText(("终端号(TERMINAL NO):"), 0, 204, (STR_FONT_VALUE_SONG), 24,
//				false, false, 0);
//		printer.prn_drawText(("  26605406"), 0, 230, (STR_FONT_VALUE_SONG), 24, false,
//				false, 0);
//		printer.prn_drawText(("卡号(CARD NO):"), 0, 256, (STR_FONT_VALUE_SONG), 24, false,
//				false, 0);
//
//		/* Black block */
//		// printer.prn_drawLine(0,380,384,380,500);
//		printer.prn_drawLine(32, 396, 352, 396, 8);
//		printer.prn_drawLine(32, 402, 352, 402, 8);
//		printer.prn_drawLine(32, 408, 352, 408, 8);
//		printer.prn_drawLine(32, 416, 352, 416, 8);
//		printer.prn_drawLine(32, 422, 352, 422, 32);

//		printer.prn_drawText(("  1234 56** ****0789"), 0, height, (STR_FONT_VALUE_SONG), 24,
//				false, false, 0);
//		height += 28;
//
//		printer.prn_drawText(("收单行号:01045840"), 0, height, (STR_FONT_VALUE_SONG), 24, false,
//				false, 0);
//		height += 28;
//
//		printer.prn_drawText(("发卡行名:渤海银行"), 0, height, (STR_FONT_VALUE_SONG), 24, false,
//				false, 0);
//		height += 28;

        printer.prn_drawText(("ABCDEFGHLIJKMNOPQXYZTRSW"), 0, height, (STR_FONT_VALUE_SONG),
                36, false, false, 0);
        height += 40;

        printer.prn_drawText(("ABCDEFGHLIJKMNOPQXYZTRSWGHLIJKMNOPQX"), 0,
                height, (STR_FONT_VALUE_SONG), 24, false, false, 0);
        height += 28;

        printer.prn_drawText(("abcdefghlijkmnopqxyztrsw"), 0, height, (STR_FONT_VALUE_SONG),
                36, false, false, 0);
        height += 40;

        printer.prn_drawText(("abcdefghlijkmnopqxyztrswefghlijkmn"), 0,
                height, (STR_FONT_VALUE_SONG), 24, false, false, 0);
        height += 28;

        printer.prn_drawText(("囎囏囐囑囒囓囔囕囖墼囏"), 0, height, (STR_FONT_VALUE_SONG), 36, false,
                false, 0);
        height += 42;

        printer.prn_drawText(("囎囏囐囑囒囓囔囕囖墼墽墾孽囎囏囓囔"), 0, height, (STR_FONT_VALUE_SONG),
                24, false, false, 0);
        height += 28;

        printer.prn_drawText(("HHHHHHHHHHHHHHHHHHHHHHHH"), 0, height, (STR_FONT_VALUE_SONG),
                36, false, false, 0);
        height += 40;

        printer.prn_drawText(("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"),
                0, height, (STR_FONT_VALUE_SONG), 24, false, false, 0);
        height += 32;

        printer.prn_drawText(("☆★○●▲△▼☆★○●▲☆★○"), 0, height, STR_FONT_VALUE_SONG, 36, false,
                false, 0);
        height += 40;

        printer.prn_drawText(("ぱばびぶづぢだざじずぜぞ"), 0, height, (STR_FONT_VALUE_SONG), 36, false,
                false, 0);
        height += 48;

        printer.prn_drawText(("㊣㈱卍▁▂▃▌▍▎▏※※㈱㊣"), 0, height, (STR_FONT_VALUE_SONG), 36, false,
                false, 0);
        height += 50;

        printer.prn_drawBarcode("12345678ABCDEF", 32, height, 20, 2, 70, 0);
		height += 80;
//
//		printer.prn_drawBarcode("12345678ABCDEF", 320, height, 20, 2, 50, 3);
    }

    public void printSale(Context context) throws Exception {


        String formaDePago ="";


//        for(TicketFormaPago item: respuestaTicketRequest.getDetalle().getTicketFormaPagos()) {
////            printer.prn_drawText(("PAGO: "+item.getFormaPago().getSatPaymentMethod().getDescription()), 0, height, (STR_FONT_VALUE_SONG),25, false, false, 0);//"PAGO: "+item.getFormaPago().getSatPaymentMethod().getDescription()
////            height += 40;
//        }



        int height = 10;
        String numeroRastreo = respuestaTicketRequest.getDetalle().getNoRastreo();
        numeroRastreo = numeroRastreo.replace("-", "");
        printer.prn_open();
        printer.prn_setupPage(_XVALUE, -1);
        printer.prn_clearPage();


        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inDensity = getResources().getDisplayMetrics().densityDpi;
        opts.inTargetDensity = getResources().getDisplayMetrics().densityDpi;
        Bitmap bitmap = BitmapFactory.decodeResource(PrintBillService.this.getResources(), R.drawable.logo, opts);


        printer.prn_drawBitmap(bitmap, 75, height);

        height += 150;
        Prn_Str("                        ",_YVALUE, height);
        height += _YVALUE;

        printer.prn_drawText((respuestaTicketRequest.getDetalle().getFecha()), 135, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 50;

        printer.prn_drawText(("Est："+db.getNumeroEstacion()), 130, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;

        printer.prn_drawText((db.getNombreEstacion()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;

//        Prn_Str(db.getCalle() + ", " + db.getNumeroExterior()+ ", " + db.getNumeroInterno()+ "\n"  + db.getColonia() + db.getLocalidad()+", "  + db.getMunicipio() + ", " + "\n" + db.getEstado() + ", " + db.getPais() + ", CP:" + db.getCP() + "\n", _YVALUE7,
//                height);
        printer.prn_drawText((db.getCalle() + ", " + db.getNumeroExterior()+ ", " + db.getNumeroInterno()+ "\n"  + db.getColonia() + db.getLocalidad()+", "  + db.getMunicipio() + ", " + "\n" + db.getEstado() + ", " + db.getPais() + ", CP:" + db.getCP() + "\n"), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 100;

        printer.prn_drawText(("RFC: " + db.getRFC()), 90, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;
        printer.prn_drawText(("SIIC: " + db.getSIIC()), 90, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;

//        Prn_Str("       SIIC：" + receive + "\n", _YVALUE6, height);
//        height += _YVALUE;
        Prn_Str("Regimen fiscla de pruebas", 22, height); //db.getRegimenFiscal()
        height += 50;
//        Prn_Str_Bold("           ORIGINAL", _YVALUE6, height);
//        height += 40;
        printer.prn_drawText(("ORIGINAL"), 125, height, (STR_FONT_VALUE_SONG),30, true, false, 0);
        height += 40;

        printer.prn_drawText(("No. Rec: " + respuestaTicketRequest.getDetalle().getNoRecibo()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 40;

        printer.prn_drawText(("No. Rastreo: "+numeroRastreo), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 40;

        printer.prn_drawText(("PC: "+ respuestaTicketRequest.getDetalle().getPosCarga()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 40;

        printer.prn_drawText(("Lo atendio: "+ respuestaTicketRequest.getDetalle().getNombreEmpleadoImpresion()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);//"PAGO: "+item.getFormaPago().getSatPaymentMethod().getDescription()
        height += 40;

        printer.prn_drawText(("------------------------"), 0, height, (STR_FONT_VALUE_SONG),50, true, false, 0);
        height += 40;

        for(TicketFormaPago item: respuestaTicketRequest.getDetalle().getTicketFormaPagos()) {

            printer.prn_drawText(("PAGO: "+item.getFormaPago().getShortDescription()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
            height += 20;
        }

        printer.prn_drawText(("------------------------"), 0, height, (STR_FONT_VALUE_SONG),50, true, false, 0);
        height += 40;

        printer.prn_drawText(("CANT  |DESC   |PRECIO    |IMPORTE"), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;
        for(TicketProducto item : respuestaTicketRequest.getDetalle().getProductos())
        {
            printer.prn_drawText(((int)item.getCantidad()+"     |"+item.getDescripcion()+"  |"+String.format("%.2f",item.getPrecio())+"     |"+String.format("%.2f",item.getImporte())), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
            height += 30;
        }



        printer.prn_drawText(("------------------------"), 0, height, (STR_FONT_VALUE_SONG),50, true, false, 0);
        height += 50;

        printer.prn_drawText(("SUBTOTAL: "+respuestaTicketRequest.getDetalle().getSubtotal()), 195, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;

        printer.prn_drawText(("IVA: "+respuestaTicketRequest.getDetalle().getIVA()), 261, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 30;

        printer.prn_drawText(("TOTAL: "+respuestaTicketRequest.getDetalle().getTotal()), 228, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 45;

        printer.prn_drawText((respuestaTicketRequest.getDetalle().getTotalTexto()), 0, height, (STR_FONT_VALUE_SONG),22, true, false, 0);
        height += 45;

        printer.prn_drawText(("$ "+respuestaTicketRequest.getDetalle().getTotal()), 110, height, (STR_FONT_VALUE_SONG),50, true, false, 0);
        height += 200;

//        printer.prn_drawBarcode("12",2,2, BarcodeFormat.QR_CODE,2,2,0);
        printer.drawBarcode("numerorastreo", 10, 10, R.drawable.qr,240,240,0);
        height += 200;

        printer.prn_drawText((respuestaTicketRequest.getPie().getMensaje().toString()), 85, height, (STR_FONT_VALUE_SONG),22, false, false, 0);
        height += 30;


        try {// 电子签名

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
//        height += _YVALUE + 80;
//        Prn_Str("本人确认以上交易,同意将其记入本卡帐户\n \n \n", 16, height);
//
//        height += _YVALUE + 10;
//        Prn_Str("  \t\t\t\t   商户存联\n", 16, height);
//
//        height += _YVALUE;
//        Prn_Str("\t  --请妥善保留小票一年--", _YVALUE6, height);
//        height += _YVALUE * 3;
        Prn_Str("\n", _YVALUE, height);
//        Prn_Str("\n", _YVALUE, height);
//        Prn_Str("\n", _YVALUE, height);
//        Prn_Str("\n", _YVALUE, height);
//        Prn_Str("", _YVALUE, height);
//        Prn_Str("", _YVALUE, height);
//        Prn_Str("", _YVALUE, height);

//		int iRet = printer.prn_printPage(0);
        enviarPrincipal();
    }

    private void enviarPrincipal() {
        Intent i = new Intent(getApplicationContext(), Menu_Principal.class);
        startActivity(i);
        onDestroy();

    }

    // 银联logo 转成Bitmap
    @SuppressWarnings("static-access")
    private Bitmap getLogoBitmap(Context context, int id) {
        BitmapDrawable draw = (BitmapDrawable) context.getResources()
                .getDrawable(id);
        Bitmap bitmap = draw.getBitmap();
        return bitmap;
    }

    private int Prn_Str(String msg, int fontSize, int height) {
        return printer.prn_drawText(msg, 0, height, STR_FONT_VALUE_SONG, fontSize, false,
                false, 0);
    }

    private int Prn_Str_Bold(String msg, int fontSize, int height) {
        return printer.prn_drawText(msg, 0, height, STR_FONT_VALUE_SONG, fontSize, true,
                false, 0);
    }

    private void sleep(){
        //延时1秒
        try {
            Thread.currentThread();
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

