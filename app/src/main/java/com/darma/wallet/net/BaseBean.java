package com.darma.wallet.net;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/12/13.
 */
public class BaseBean implements Serializable {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
