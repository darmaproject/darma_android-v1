/**
 * Copyright 2019 darma.com
 */
package com.wallet.bean;

import com.google.gson.Gson;
import com.wallet.model.Coin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class WalletStatusInfoBean implements Serializable {

    private long unlocked_balance;
    private long locked_balance;
    private long total_balance;
    private long wallet_height;
    private long daemon_height;
    private long wallet_topo_height;
    private long daemon_topo_height;
    private long wallet_initial_height;
    private boolean wallet_available;
    private boolean wallet_complete;
    private boolean wallet_online;
    private long wallet_mixin;
    private double wallet_fees_multiplier;
    private long wallet_sync_time;
    private long wallet_minimum_topo_height;
    public void setUnlocked_balance(long unlocked_balance) {
        this.unlocked_balance = unlocked_balance;
    }
    public Coin getUnlocked_balance() {
        return Coin.valueOf(unlocked_balance);
    }

    public void setLocked_balance(long locked_balance) {
        this.locked_balance = locked_balance;
    }
    public Coin getLocked_balance() {
        return Coin.valueOf(locked_balance);
    }

    public void setTotal_balance(long total_balance) {
        this.total_balance = total_balance;
    }
    public Coin getTotal_balance() {
        return Coin.valueOf(total_balance);
    }

    public void setWallet_height(long wallet_height) {
        this.wallet_height = wallet_height;
    }
    public long getWallet_height() {
        return wallet_height;
    }

    public void setDaemon_height(long daemon_height) {
        this.daemon_height = daemon_height;
    }
    public long getDaemon_height() {
        return daemon_height;
    }

    public void setWallet_topo_height(long wallet_topo_height) {
        this.wallet_topo_height = wallet_topo_height;
    }
    public long getWallet_topo_height() {
        return wallet_topo_height;
    }

    public void setDaemon_topo_height(long daemon_topo_height) {
        this.daemon_topo_height = daemon_topo_height;
    }
    public long getDaemon_topo_height() {
        return daemon_topo_height;
    }

    public void setWallet_initial_height(long wallet_initial_height) {
        this.wallet_initial_height = wallet_initial_height;
    }
    public long getWallet_initial_height() {
        return wallet_initial_height;
    }

    public void setWallet_available(boolean wallet_available) {
        this.wallet_available = wallet_available;
    }
    public boolean getWallet_available() {
        return wallet_available;
    }

    public void setWallet_complete(boolean wallet_complete) {
        this.wallet_complete = wallet_complete;
    }
    public boolean getWallet_complete() {
        return wallet_complete;
    }

    public void setWallet_online(boolean wallet_online) {
        this.wallet_online = wallet_online;
    }
    public boolean getWallet_online() {
        return wallet_online;
    }

    public void setWallet_mixin(long wallet_mixin) {
        this.wallet_mixin = wallet_mixin;
    }
    public long getWallet_mixin() {
        return wallet_mixin;
    }

    public void setWallet_fees_multiplier(double wallet_fees_multiplier) {
        this.wallet_fees_multiplier = wallet_fees_multiplier;
    }
    public double getWallet_fees_multiplier() {
        return wallet_fees_multiplier;
    }

    public void setWallet_sync_time(long wallet_sync_time) {
        this.wallet_sync_time = wallet_sync_time;
    }
    public long getWallet_sync_time() {
        return wallet_sync_time;
    }

    public void setWallet_minimum_topo_height(long wallet_minimum_topo_height) {
        this.wallet_minimum_topo_height = wallet_minimum_topo_height;
    }
    public long getWallet_minimum_topo_height() {
        return wallet_minimum_topo_height;
    }


    public String getSynchronousProgress(){
        if(daemon_topo_height!=0){

            return ""+((float)((long)(wallet_topo_height*10000/daemon_topo_height)))/100+"%";
        }

        return "";
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}