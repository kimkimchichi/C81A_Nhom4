package com.OOD.malissa.shoopingcart.Models;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;

import java.io.Serializable;
import java.util.ArrayList;


abstract class Account implements Serializable{

    protected String _username;
    protected String _password;
    protected User _accountType;
    public String getUsername(){

        return _username;
    }

    public String getPassword(){

        return _password;
    }

    public ArrayList<String> toArrayList() {
        ArrayList<String> accInfo = new ArrayList<>();
        accInfo.add(_username);
        accInfo.add(_password);
        return accInfo;
    }

}
