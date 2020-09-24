/**
  * Copyright 2019 darma.com
  */
package com.wallet.bean;
import com.wallet.model.Coin;

import java.io.Serializable;
import java.util.List;

public class TransactionDetailsBean implements Serializable {

    private String txid;
    private long fees;
    private List<Long> amount;
    private List<String> to;
    private String tx_secret_key;
    public void setTxid(String txid) {
         this.txid = txid;
     }
     public String getTxid() {
         return txid;
     }

    public void setFees(long fees) {
         this.fees = fees;
     }
     public Coin getFees() {
         return Coin.valueOf(fees);
     }

    public void setAmount(List<Long> amount) {
         this.amount = amount;
     }
     public List<Long> getAmount() {
         return amount;
     }

    public void setTo(List<String> to) {
         this.to = to;
     }
     public List<String> getTo() {
         return to;
     }

    public void setTx_secret_key(String tx_secret_key) {
         this.tx_secret_key = tx_secret_key;
     }
     public String getTx_secret_key() {
         return tx_secret_key;
     }

}