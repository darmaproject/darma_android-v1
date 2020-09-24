/**
 * Copyright 2019 darma.com
 */
package com.wallet.bean;

public class PrivateKey {

    private String Spendkey_Secret;
    private String Spendkey_Public;
    private String Viewkey_Secret;
    private String Viewkey_Public;
    public void setSpendkey_Secret(String Spendkey_Secret) {
        this.Spendkey_Secret = Spendkey_Secret;
    }
    public String getSpendkey_Secret() {
        return Spendkey_Secret;
    }

    public void setSpendkey_Public(String Spendkey_Public) {
        this.Spendkey_Public = Spendkey_Public;
    }
    public String getSpendkey_Public() {
        return Spendkey_Public;
    }

    public void setViewkey_Secret(String Viewkey_Secret) {
        this.Viewkey_Secret = Viewkey_Secret;
    }
    public String getViewkey_Secret() {
        return Viewkey_Secret;
    }

    public void setViewkey_Public(String Viewkey_Public) {
        this.Viewkey_Public = Viewkey_Public;
    }
    public String getViewkey_Public() {
        return Viewkey_Public;
    }

}