package com.darma.wallet.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class OrderDB implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String base_coin;
    private String quota_coin;

    private String base_amount_remaining;
    private String base_amount_total;
    private long base_num_confirmations_remaining;
    private String base_receiving_address;
    private String base_receiving_integrated_address;
    private long base_recommended_mixin;
    private String base_received_amount;
    private String base_required_payment_id_long;
    private String base_required_payment_id_short;
    private String created_at;
    private String expires_at;
    private String final_price;
    private String order_id;
    private String order_price;
    private String pair;
    private String quota_amount;
    private String quota_dest_address;
    private long quota_num_confirmations;
    private long quota_num_confirmations_before_purge;
    private String quota_real_amount;
    private String quota_transaction_id;
    private long seconds_till_timeout;
    private String state;
    private String state_string;
    private String refund_address;
    private String refund_amount;
    private String base_transaction_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBase_coin() {
        return base_coin;
    }

    public void setBase_coin(String base_coin) {
        this.base_coin = base_coin;
    }

    public String getQuota_coin() {
        return quota_coin;
    }

    public void setQuota_coin(String quota_coin) {
        this.quota_coin = quota_coin;
    }

    public void setBase_amount_remaining(String base_amount_remaining) {
        this.base_amount_remaining = base_amount_remaining;
    }

    public String getBase_amount_remaining() {
        return base_amount_remaining;
    }

    public void setBase_amount_total(String base_amount_total) {
        this.base_amount_total = base_amount_total;
    }

    public String getBase_amount_total() {
        return base_amount_total;
    }

    public void setBase_num_confirmations_remaining(long base_num_confirmations_remaining) {
        this.base_num_confirmations_remaining = base_num_confirmations_remaining;
    }

    public long getBase_num_confirmations_remaining() {
        return base_num_confirmations_remaining;
    }

    public void setBase_receiving_address(String base_receiving_address) {
        this.base_receiving_address = base_receiving_address;
    }

    public String getBase_receiving_address() {
        return base_receiving_address;
    }

    public void setBase_receiving_integrated_address(String base_receiving_integrated_address) {
        this.base_receiving_integrated_address = base_receiving_integrated_address;
    }

    public String getBase_receiving_integrated_address() {
        return base_receiving_integrated_address;
    }

    public void setBase_recommended_mixin(long base_recommended_mixin) {
        this.base_recommended_mixin = base_recommended_mixin;
    }

    public long getBase_recommended_mixin() {
        return base_recommended_mixin;
    }

    public void setBase_received_amount(String base_received_amount) {
        this.base_received_amount = base_received_amount;
    }

    public String getBase_received_amount() {
        return base_received_amount;
    }

    public void setBase_required_payment_id_long(String base_required_payment_id_long) {
        this.base_required_payment_id_long = base_required_payment_id_long;
    }

    public String getBase_required_payment_id_long() {
        return base_required_payment_id_long;
    }

    public void setBase_required_payment_id_short(String base_required_payment_id_short) {
        this.base_required_payment_id_short = base_required_payment_id_short;
    }

    public String getBase_required_payment_id_short() {
        return base_required_payment_id_short;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getPair() {
        return pair;
    }

    public void setQuota_amount(String quota_amount) {
        this.quota_amount = quota_amount;
    }

    public String getQuota_amount() {
        return quota_amount;
    }

    public void setQuota_dest_address(String quota_dest_address) {
        this.quota_dest_address = quota_dest_address;
    }

    public String getQuota_dest_address() {
        return quota_dest_address;
    }

    public void setQuota_num_confirmations(long quota_num_confirmations) {
        this.quota_num_confirmations = quota_num_confirmations;
    }

    public long getQuota_num_confirmations() {
        return quota_num_confirmations;
    }

    public void setQuota_num_confirmations_before_purge(long quota_num_confirmations_before_purge) {
        this.quota_num_confirmations_before_purge = quota_num_confirmations_before_purge;
    }

    public long getQuota_num_confirmations_before_purge() {
        return quota_num_confirmations_before_purge;
    }

    public void setQuota_real_amount(String quota_real_amount) {
        this.quota_real_amount = quota_real_amount;
    }

    public String getQuota_real_amount() {
        return quota_real_amount;
    }

    public void setQuota_transaction_id(String quota_transaction_id) {
        this.quota_transaction_id = quota_transaction_id;
    }

    public String getQuota_transaction_id() {
        return quota_transaction_id;
    }

    public void setSeconds_till_timeout(long seconds_till_timeout) {
        this.seconds_till_timeout = seconds_till_timeout;
    }

    public long getSeconds_till_timeout() {
        return seconds_till_timeout;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState_string(String state_string) {
        this.state_string = state_string;
    }

    public String getState_string() {
        return state_string;
    }

    public void setRefund_address(String refund_address) {
        this.refund_address = refund_address;
    }

    public String getRefund_address() {
        return refund_address;
    }

    public void setRefund_amount(String refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getRefund_amount() {
        return refund_amount;
    }

    public void setBase_transaction_id(String base_transaction_id) {
        this.base_transaction_id = base_transaction_id;
    }

    public String getBase_transaction_id() {
        return base_transaction_id;
    }


}
