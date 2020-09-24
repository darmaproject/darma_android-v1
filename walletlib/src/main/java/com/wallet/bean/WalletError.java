package com.wallet.bean;

import appwallet.Appwallet;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/9/29.
 */
public class WalletError   implements Serializable {

    public long errCode;
    public String  errMsg;

    private WalletError(){

    }
    public static WalletError from(String json){
        return  new Gson().fromJson(json, WalletError.class);
    }
    public static WalletError create(long errCode,String errMsg){
        WalletError walletError=new WalletError();
        walletError.errCode=errCode;
        walletError.errMsg=errMsg;
        return  walletError;
    }

    @Override
    public String toString() {
        return "errCode = "+errCode+"   errMsg = "+errMsg;
    }
}
