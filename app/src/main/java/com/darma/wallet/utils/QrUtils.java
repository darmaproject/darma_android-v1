package com.darma.wallet.utils;

import android.graphics.Bitmap;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.wallet.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Darma Project on 2019/10/8.
 */
public class QrUtils {
    public static final String URI_ADDRESS="darma";
    public static final String URI_AMOUNT="tx_amount";
    public static final String URI_PAYMENT_ID="tx_payment_id";


    public static Bitmap encodeAsBitmap(String str, int widht, int height, int qrColor, int backgroundColor) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, widht, height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? qrColor : backgroundColor;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }


    public static String toShareQRuri(String address, String amount, String payment_id) {

        return URI_ADDRESS+":"+address+"?"+URI_PAYMENT_ID+"="+payment_id+"&"+URI_AMOUNT+"="+amount;
    }


    public static boolean checkDarmaUri(String uri){

        if(!StringUtils.isEmpty(uri)){
            if(uri.startsWith(URI_ADDRESS+":")){
                return true;
            }
        }
        return false;
    }
    //darma:4F3FHTDWmXPRy92JYFke2fvQXReTMvxr2uSbPNuFPhc?tx_payment_id=58f02bf1bbe9f4c6&tx_amount=455.2451
    public static Map<String,String> getUriParams(String url) {
        Map<String,String> map=new HashMap<>();

        String address=getUriAddress(url);
        if(!StringUtils.isEmpty(address)){
            map.put(URI_ADDRESS,address);
        }
        map.putAll(UrlUtil.URLRequest(url));
        return map;
    }

    public static String getUriAddress(String url){
        String front=UrlUtil.UrlPage(url);

        if(StringUtils.isEmpty(front)){
            return url;
        }
        String[] arrSplit = null;
        arrSplit=front.split("[:]");
        if(front.length() > 0){
            if(arrSplit.length > 1){
                return arrSplit[1];
            }
        }
        return url;
    }
}
