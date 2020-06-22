package com.OOD.malissa.shoopingcart.Models;

import java.io.Serializable;

public class CreditCard implements Serializable {

    private String _accNumber;
    private String _expiration;

    public CreditCard(String accountNum, String expiry){

        this._accNumber = accountNum;
        this._expiration = expiry;
    }


    public String getAccNumber() {return _accNumber;}

    public String getExpiration() { return _expiration;}

}
