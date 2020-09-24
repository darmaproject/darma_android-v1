package com.wallet.model;

import com.google.gson.Gson;
import com.wallet.bean.WalletError;

public class WalletErrorException extends Exception {
    private WalletError error;

    public WalletErrorException(WalletError error) {
        super(error.toString());

        this.error = error;
    }

    public WalletError getError() {
        return error;
    }

}