package com.OOD.malissa.shoopingcart.Models;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;

import java.util.ArrayList;

public class BuyerAccount extends Account {

    private ArrayList<CreditCard> _cCards;
    private double _bill;

    public BuyerAccount(String username, String password){

        super._username = username;
        super._password = password;
        super._accountType = User.BUYER;
        this._bill = 0.0;
        this._cCards = new ArrayList<CreditCard>();

    }


    public double getBill() {return _bill; }

    public ArrayList<CreditCard> getcCards() {return _cCards;}

    @Override
    public String getUsername() { return super._username;}

    @Override
    public String getPassword() { return super._password;}

    public User getAccountType() { return super._accountType;}

    public void setBill(double bill) {this._bill += bill;}

    public void setUsername(String username) { super._username = username;}

    public void setPassword(String password) { super._password = password;}

    public void addcCard(String accountNum, String expiry){
        CreditCard card = new CreditCard(accountNum,expiry);
        this._cCards.add(card);
    }
}
