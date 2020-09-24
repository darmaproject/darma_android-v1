package com.darma.wallet.bean;

import com.darma.wallet.net.BaseBean;
import com.wallet.model.Coin;

import java.math.BigDecimal;

public class PairInfoBean extends BaseBean {

    private Data data;


    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }


    public class Data {

        private String price;
        private String quota_lower_limit;
        private String quota_upper_limit;

        private int quota_scale;
        private int base_scale;


        public int getQuota_scale() {
            return quota_scale;
        }

        public void setQuota_scale(int quota_scale) {
            this.quota_scale = quota_scale;
        }

        public int getBase_scale() {
            return base_scale;
        }

        public void setBase_scale(int base_scale) {
            this.base_scale = base_scale;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice() {
            return price;
        }

        public void setQuota_lower_limit(String quota_lower_limit) {
            this.quota_lower_limit = quota_lower_limit;
        }

        public String getQuota_lower_limit() {
            return quota_lower_limit;
        }

        public void setQuota_upper_limit(String quota_upper_limit) {
            this.quota_upper_limit = quota_upper_limit;
        }

        public String getQuota_upper_limit() {
            return quota_upper_limit;
        }

    }
}