package com.darma.wallet.net;

import com.darma.wallet.MyApplication;
import com.darma.wallet.utils.LanguageUtil;
import com.orhanobut.logger.Logger;
import com.wallet.utils.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;


public class HttpHelper {

    private static final String TAG = "HttpHelper";

    private static final int ERROR_NETWORK = -1;
    private static final int ERROR_DATA = -2;


    public static final int SUCCESS_CODE=200;
    public static final int FAIL_CODE =400;


//    public static void sendRequestGET(String url, final BaseOnResultListener listener) {
//
//        sendRequestGET(url,new HashMap<>(),listener);
//    }
//
//    public static void sendRequestGET( String url,Map<String, String> params, final BaseOnResultListener listener) {
//
//        sendRequestGET(url,new HashMap<>(),params,listener);
//    }
//
//
//    public static void sendRequestPOST( String url, final BaseOnResultListener listener) {
//
//        sendRequestPOST(url,new HashMap<>(),listener);
//    }
//
//    public static void sendRequestPOST( String url,Map<String, String> params, final BaseOnResultListener listener) {
//
//        sendRequestPOST(url,new HashMap<>(),params,listener);
//    }

    //    public static void sendRequestPOST(String url, Map<String, String> headers,Map<String, String> params, final BaseOnResultListener listener){
//
//        sendRequestPOST(url, headers, params, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
////                listener.onFailure(e.getMessage());
////                e.printStackTrace();
////                listener.onFailure(XApplication.getInstance().getString(R.string.str_network_error));
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
////                ApiHelper.handleJson(response, new ApiHelper.HandleResult() {
////                    @Override
////                    public void onResultOk(String result) {
////                        listener.onSuccess(result);
////                    }
////
////                    @Override
////                    public void onResultError(String msg) {
////                        listener.onFailure(msg);
////                    }
////
////                    @Override
////                    public void onResultException(String exception) {
////                        listener.onFailure(exception);
////                    }
////                });
//            }
//        });
//    }
    public static void sendRequestPOST(String url, Map<String, String> headers, Map<String, String> params, final StringCallback listener) {

        String timeStamp = String.valueOf(System.currentTimeMillis());


        try {
            OkHttpUtils
                    .post()
                    .url(url)
                    .headers(headers)
                    .params(params)
                    .addHeader("app-language", getAppLanguage())
                    .addHeader("BT-ACCESS-TIMESTAMP", timeStamp)
                    .addHeader("BT-ACCESS-SIGN", ApiNetConfig.signParams(timeStamp, "POST", params))
                    .build()
                    .execute(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String getAppLanguage() {
        String locale = LanguageUtil.getAppLanguage(MyApplication.Companion.getContext());

        return locale;
    }
    public static void sendRequestGET(String url, Map<String, String> headers, Map<String, String> params, final StringCallback listener) {
        String timeStamp = String.valueOf(System.currentTimeMillis());

        try {
            OkHttpUtils
                    .get()
                    .url(url)
                    .headers(headers)
                    .params(params)
                    .addHeader("app-language", getAppLanguage())
                    .addHeader("BT-ACCESS-TIMESTAMP", timeStamp)
                    .addHeader("BT-ACCESS-SIGN", ApiNetConfig.signParams(timeStamp, "POST", params))
                    .build()
                    .execute(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void sendRequestGET(String url, Map<String, String> headers,Map<String, String> params, final BaseOnResultListener listener){
//
//        sendRequestGET(url, headers, params, new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                listener.onFailure(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(String response, int id) {
//                ApiHelper.handleJson(response, new ApiHelper.HandleResult() {
//                    @Override
//                    public void onResultOk(String result) {
//                        listener.onSuccess(result);
//                    }
//
//                    @Override
//                    public void onResultError(String msg) {
//                        listener.onFailure(msg);
//                    }
//
//                    @Override
//                    public void onResultException(String exception) {
//                        listener.onFailure(exception);
//                    }
//                });
//            }
//        });
//    }


    public static RequestBuilder get() {
        return new GetRequest();
    }

    public static RequestBuilder post() {
        return new PostRequest();
    }


    public static class PostRequest<T extends BaseBean> extends RequestBuilder {


        public void send() {
            if (mOnResultListener != null) {
                sendRequestPOST(url, headers, params, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mOnResultListener.onFailure(ERROR_NETWORK,e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        T bean = (T) GsonUtils.gsonToBean(response, cls);
                        if (bean != null && bean.getCode() == SUCCESS_CODE) {

                            mOnResultListener.onSuccess(bean);

                        } else if (bean != null&&bean.getMessage()!=null) {

                            mOnResultListener.onFailure(bean.getCode(),bean.getMessage());
                        }else{

                            mOnResultListener.onFailure(ERROR_DATA,bean.getMessage());
                        }
                    }
                });
            }
//            else
//            if(mStringCallback!=null){
//                sendRequestPOST(url,headers,params,mStringCallback );
//            }
        }

    }

    public static class GetRequest<T extends BaseBean> extends RequestBuilder {


        public void send() {
            if (mOnResultListener != null) {
                sendRequestGET(url, headers, params,  new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mOnResultListener.onFailure(ERROR_NETWORK,e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        T bean = (T) GsonUtils.gsonToBean(response, cls);
                        if (bean != null && bean.getCode() == SUCCESS_CODE) {

                            mOnResultListener.onSuccess(bean);

                        } else if (bean != null&&bean.getMessage()!=null) {

                            mOnResultListener.onFailure(bean.getCode(),bean.getMessage());
                        }else{

                            mOnResultListener.onFailure(ERROR_DATA,bean.getMessage());
                        }
                    }
                });
            }
//            if(mStringCallback!=null){
//                sendRequestGET(url,headers,params,mStringCallback );
//            }
        }


    }

    public static abstract class RequestBuilder<T extends BaseBean> {

        Map<String, String> params;
        Map<String, String> headers;

        String url;
        BaseOnResultListener mOnResultListener;
        StringCallback mStringCallback;

        Class<T> cls;

        private RequestBuilder() {
            params = new HashMap<>();
            headers = new HashMap<>();
        }


        public RequestBuilder addParams(String key, String value) {
            params.put(key, value);
            return this;
        }

        public RequestBuilder addHeader(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public RequestBuilder url(String url) {
            this.url = url;
            return this;
        }
        public void execute(BaseOnResultListener<T> mOnResultListener,Class<T> cls) {
            this.mOnResultListener = mOnResultListener;
            this.cls=cls;
            send();
        }
//        public void execute(StringCallback mStringCallback){
//            this.mStringCallback=mStringCallback;
//            send();
//        }
//        public RequestBuilder callBack(BaseOnResultListener mOnResultListener){
//            this.mOnResultListener=mOnResultListener;
//            return this;
//        }
//

//        public RequestBuilder build(){
//            return this;
//        }

        public abstract void send();

    }



}
