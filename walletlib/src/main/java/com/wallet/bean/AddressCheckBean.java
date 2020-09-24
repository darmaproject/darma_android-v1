/**
  * Copyright 2019 darma.com
  */
package com.wallet.bean;

public class AddressCheckBean {

    private boolean isValid;
    private boolean isIntegratedAddress;
    private String address;
    private String paymentID;
    public void setIsValid(boolean isValid) {
         this.isValid = isValid;
     }
     public boolean getIsValid() {
         return isValid;
     }

    public void setIsIntegratedAddress(boolean isIntegratedAddress) {
         this.isIntegratedAddress = isIntegratedAddress;
     }
     public boolean getIsIntegratedAddress() {
         return isIntegratedAddress;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setPaymentID(String paymentID) {
         this.paymentID = paymentID;
     }
     public String getPaymentID() {
         return paymentID;
     }

}