package com.darma.wallet.net;


public interface BaseOnResultListener<T extends BaseBean> {
    void onSuccess(T bean);

    void onFailure(int code,String error);
}
