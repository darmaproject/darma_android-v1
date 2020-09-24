/**
 * Copyright 2019 darma.com
 */
package com.wallet.bean;
import com.wallet.model.Coin;

import java.io.Serializable;
import java.util.Date;

public class TransactionRecordBean implements Serializable {

    private long index_global;
    private long height;
    private long topoheight;
    private String txid;
    private long amount;
    private String payment_id;
    private String payment_id_str;
    private int status;
    private int unlock_status;
    private int unlock_time;
    private Date time;
    private String secret_tx_key;
    private TransactionDetailsBean details;
    public void setIndex_global(long index_global) {
        this.index_global = index_global;
    }
    public long getIndex_global() {
        return index_global;
    }

    public void setHeight(long height) {
        this.height = height;
    }
    public long getHeight() {
        return height;
    }

    public void setTopoheight(long topoheight) {
        this.topoheight = topoheight;
    }
    public long getTopoheight() {
        return topoheight;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
    public String getTxid() {
        return txid;
    }

    public int getUnlock_status() {
        return unlock_status;
    }

    public void setUnlock_status(int unlock_status) {
        this.unlock_status = unlock_status;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
    public Coin getAmount() {
        return Coin.valueOf(amount);
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
    public String getPayment_id() {
        return payment_id;
    }

    public String getPayment_id_str() {
        return payment_id_str;
    }

    public void setPayment_id_str(String payment_id_str) {
        this.payment_id_str = payment_id_str;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setUnlock_time(int unlock_time) {
        this.unlock_time = unlock_time;
    }
    public int getUnlock_time() {
        return unlock_time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    public Date getTime() {
        return time;
    }

    public void setSecret_tx_key(String secret_tx_key) {
        this.secret_tx_key = secret_tx_key;
    }
    public String getSecret_tx_key() {
        return secret_tx_key;
    }

    public void setDetails(TransactionDetailsBean details) {
        this.details = details;
    }
    public TransactionDetailsBean getDetails() {
        return details;
    }


}