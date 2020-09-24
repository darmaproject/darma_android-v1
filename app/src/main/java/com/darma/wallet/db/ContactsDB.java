package com.darma.wallet.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Darma Project on 2019/9/30.
 */
@Entity
public class ContactsDB implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String remark;

    private String address;

    private String coin_code;
    private String payment_id;
    private String desc;
    private long createTime;

    private long updateTime;


    public ContactsDB(){

    }


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCoin_code() {
        return coin_code;
    }

    public void setCoin_code(String coin_code) {
        this.coin_code = coin_code;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
