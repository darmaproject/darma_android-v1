package com.darma.wallet.bean;

import com.wallet.model.Coin;

/**
 * Created by Darma Project on 2019/11/5.
 */
public class OutputDetial {
    private long amount;
    private String address;

    public Coin getAmount() {
        return Coin.valueOf(amount);
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
