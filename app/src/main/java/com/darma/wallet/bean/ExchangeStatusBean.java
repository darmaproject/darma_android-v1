package com.darma.wallet.bean;

import com.darma.wallet.net.BaseBean;

/**
 * Created by Darma Project on 2019/12/30.
 */
public class ExchangeStatusBean extends BaseBean {

    private Data data;


    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }


    public class Data {

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

