package com.darma.wallet.bean;

import com.darma.wallet.db.OrderDB;
import com.darma.wallet.net.BaseBean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Darma Project on 2019/12/13.
 */
public class OrderBean extends BaseBean {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends OrderDB implements Serializable {





    }



    public class Status{
        /**
         * TIMED_OUT：
         * UNPAID：
         * PAID_UNCONFIRMED：
         * PAID：
         * SENDING：
         * SUCCESS：
         * FAILED_REFUNDED：
         * FAILED_UNREFUND：
         */

        public static final String TIMED_OUT="TIMED_OUT";
        public static final String UNPAID="UNPAID";
        public static final String PAID_UNCONFIRMED="PAID_UNCONFIRMED";
        public static final String PAID="PAID";
        public static final String SENDING="SENDING";
        public static final String SUCCESS="SUCCESS";
        public static final String FAILED_REFUNDED="FAILED_REFUNDED";
        public static final String FAILED_UNREFUND="FAILED_UNREFUND";
    }


}
