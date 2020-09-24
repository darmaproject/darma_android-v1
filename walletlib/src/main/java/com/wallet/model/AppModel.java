package com.wallet.model;

import com.wallet.bean.Node;
import com.wallet.bean.WalletStatusInfoBean;
import com.wallet.listener.WalletStatusInfoListener;

/**
 * Created by Darma Project on 2019/10/25.
 */
public interface AppModel {

    Node getSelectNode();
    void saveSelectNode(Node node);
    void saveWalletToDB(Wallet wallet);
    void onWalletStatusInfo(WalletStatusInfoBean info);

}
