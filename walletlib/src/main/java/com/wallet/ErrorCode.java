package com.wallet;

import appwallet.Appwallet;

/**
 * Created by Darma Project on 2019/10/16.
 */
public class ErrorCode {



    public static final long DEFALUT=-1;

    public static long getErrAgain() {
        return Appwallet.getErrAgain();
    }


    public static long getErrBadWalletFile() {
        return Appwallet.getErrBadWalletFile();
    }


    public static long getErrDaemonIsEmpty() {
        return Appwallet.getErrDaemonIsEmpty();
    }


    public static long getErrDecodeData() {
        return Appwallet.getErrDecodeData();
    }


    public static long getErrEncodeData() {
        return Appwallet.getErrEncodeData();
    }


    public static long getErrExist() {
        return Appwallet.getErrExist();
    }


    public static long getErrInvalidAddress() {
        return Appwallet.getErrInvalidAddress();
    }


    public static long getErrInvalidAmount() {
        return Appwallet.getErrInvalidAmount();
    }


    public static long getErrInvalidDaemon() {
        return Appwallet.getErrInvalidDaemon();
    }


    public static long getErrInvalidFileName() {
        return Appwallet.getErrInvalidFileName();
    }


    public static long getErrInvalidKeys() {
        return Appwallet.getErrInvalidKeys();
    }


    public static long getErrInvalidPassword() {
        return Appwallet.getErrInvalidPassword();
    }


    public static long getErrInvalidPaymentID() {
        return Appwallet.getErrInvalidPaymentID();
    }


    public static long getErrInvalidWalletObject() {
        return Appwallet.getErrInvalidWalletObject();
    }


    public static long getErrIsSync() {
        return Appwallet.getErrIsSync();
    }


    public static long getErrNotIsSync() {
        return Appwallet.getErrNotIsSync();
    }


    public static long getErrPaswordMisMatch() {
        return Appwallet.getErrPaswordMisMatch();
    }


    public static long getErrSuccess() {
        return Appwallet.getErrSuccess();
    }


    public static long getErrSystemInternal() {
        return Appwallet.getErrSystemInternal();
    }


    public static long getErrWalletBusy() {
        return Appwallet.getErrWalletBusy();
    }


}
