package com.wallet.bean;

import com.google.gson.Gson;
import com.wallet.model.Coin;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/10/9.
 */
public class Transaction implements Serializable {



    private String txid;
    private long fee;
    private long txraw;
    private long input_sum;
    private String change;
    private String transfer_txhex;



    private String receive_address;

    private String payment_id;
    private long amount;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public Coin getAmount() {
        return Coin.valueOf(amount);
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }


    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
    public String getTxid() {
        return txid;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
    public Coin getFee() {
        return Coin.valueOf(fee);
    }

    public void setTxraw(long txraw) {
        this.txraw = txraw;
    }
    public long getTxraw() {
        return txraw;
    }

    public void setInput_sum(long input_sum) {
        this.input_sum = input_sum;
    }
    public long getInput_sum() {
        return input_sum;
    }

    public void setChange(String change) {
        this.change = change;
    }
    public String getChange() {
        return change;
    }

    public void setTransfer_txhex(String transfer_txhex) {
        this.transfer_txhex = transfer_txhex;
    }
    public String getTransfer_txhex() {
        return transfer_txhex;
    }


    public static Transaction from(String json){
        return new Gson().fromJson(json,Transaction.class);
    }
}
