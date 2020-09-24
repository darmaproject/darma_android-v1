package com.wallet.model;

import com.google.gson.Gson;

import java.math.BigDecimal;


public class Rate {

    static {
        NEGATIVE= new Rate(null,null,0);
    }
    public static final Rate NEGATIVE;//Invalid data

    /** Coin letters (USD,EUR,etc..) */
    private final String code;

    private final BigDecimal rate;
    /** Last update time */
    private final long timestamp;


    public Rate(String code, BigDecimal rate, long timestamp) {
        this.code = code;
        this.rate = rate;
        this.timestamp = timestamp;

    }

    public String getCode() {
        return code;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Old method..
     */
    public String getLink(){
        return null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Rate fromString(String str){
        return new Gson().fromJson(str,Rate.class);
    }
}
