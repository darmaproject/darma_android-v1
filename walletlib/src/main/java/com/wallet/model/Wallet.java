package com.wallet.model;

import com.wallet.WalletConfig;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Darma Project on 2019/9/24.
 */
public class Wallet implements Serializable {



    private File walletFile;
    private String  pwd;


    public Wallet(File walletFile) {
        this.walletFile = walletFile;

    }


    public File getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(File walletFile) {
        this.walletFile = walletFile;
    }

    public String getPath() {
        return walletFile.getAbsolutePath();
    }


    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName(){
//       return  mWalletDB.getName();
        return walletFile.getName().replace(WalletConfig.WALLET_FILE_SUFFIX,"");
    }




}
