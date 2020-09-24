package com.darma.wallet.net;

import com.google.common.base.Joiner;
import com.orhanobut.logger.Logger;
import com.wallet.utils.GsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Darma Project on 2019/12/11.
 */
public class ApiNetConfig {


    public static final String API_SECRET_KEY="api_secret_key";

    public static final String NET_URL="";



    public static final String EXCHANGE_STATUS=NET_URL+"api/v1/exchange_status";

    public static final String ORDER_PARAMETER_QUERY=NET_URL+"api/v1/order_parameter_query";

    public static final String ORDER_CREATE=NET_URL+"api/v1/order_create";

    public static final String ORDER_CHECK_PRICE=NET_URL+"api/v1/order_check_price";

    public static final String ORDER_STATUS_QUERY=NET_URL+"api/v1/order_status_query";


    public static String HMACSHA256(String data) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(API_SECRET_KEY.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }


    public static String signParams(String timeStamp,String methed,Map<String,String> params) throws Exception {

        String json= mapToFormData(params,false);

        String sign=timeStamp+methed+json;

//        Logger.w("sign = "+sign);
        return HMACSHA256(sign);
    }

    public static String mapToFormData(Map<String, String> map, boolean isURLEncoder) throws UnsupportedEncodingException {
        String formData = "";
        if (map != null && map.size() > 0) {
            formData = Joiner.on("&").withKeyValueSeparator("=").join(map);
            if (isURLEncoder) {
                formData = URLEncoder.encode(formData, "UTF-8");
            }
        }
        return formData;
    }
}
