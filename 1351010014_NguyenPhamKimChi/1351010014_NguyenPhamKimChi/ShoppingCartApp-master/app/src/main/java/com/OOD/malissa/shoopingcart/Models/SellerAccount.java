package com.OOD.malissa.shoopingcart.Models;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Models.HelperClasses.IDSellerName;
import com.OOD.malissa.shoopingcart.Models.Interfaces.IDAlgorithm;
import com.OOD.malissa.shoopingcart.Models.Interfaces.priceObserver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class SellerAccount extends Account implements priceObserver{

    private double _costs;
    private double _profits;
    private double _revenues;
    private String _sellerID;
    private Inventory _items;
    private static int prepopCount = 0;
    private IDAlgorithm _sellerIDAlgo;
    private DecimalFormat df = new DecimalFormat("0.00");

    public SellerAccount(String username, String password){

        super._username = username;
        super._password = password;
        super._accountType = User.SELLER;
        this._costs = 0.0;
        this._profits = 0.0;
        this._revenues = 0.0;
        this._items = new Inventory();
        this._items.setPriceObserver(this);

        _sellerIDAlgo = new IDSellerName();

        calculateSellerID();
    }

    public void removeItem(Product item)
    {
        this._items.removeItem(item);
    }

    private void calculateSellerID() {

        this._sellerID = _sellerIDAlgo.calculate(super._username);


    }

    public String calculateProductID() {

        return this._items.getNewProductID(this._sellerID);
    }

    public void addProduct(Product item){
        this._items.addItem(item);
    }

    public String getFinances() {
        String financeInfo = "";
            financeInfo += "Profit:\t\t\t$ " + df.format(this.get_profits())+ "\n";
       financeInfo += "Cost:\t\t\t$ " + df.format(this.get_costs())+ "\n";
        financeInfo += "Revenue:\t\t$ " + df.format(this.get_revenues())+ "\n";

        return financeInfo;
    }

    public void prepopulateInventory(){
        ArrayList<Product> temp = new ArrayList<>();

        temp.add(new Product("T-Shirts", "1", "\"It hits things\"", "Tools", 5, 3.00, 5.00, this._sellerID));
        temp.add(new Product("Short", "2", "\"It holds things together\"", "Tools", 3, 1.00, 2.00, this._sellerID));
        temp.add(new Product("Trouser", "3", "\"It washes things\"", "Hygiene", 30, 2.00, 6.00, this._sellerID));
        temp.add(new Product("Skirt", "4", "\"It looks nice\"", "Decor", 15, 10.00, 15.00, this._sellerID));
        temp.add(new Product("Jeans", "5", "\"It Makes you smarter\"", "Books", 20, 20.00, 50.00, this._sellerID));
        temp.add(new Product("Swimwear", "6", "\"It's a treat\"", "Food", 40, 0.50, 2.00, this._sellerID));
        temp.add(new Product("Accessorie", "7", "\"Comes from chickens\"", "Food", 55, 0.20, 0.70, this._sellerID));
        temp.add(new Product("Jacket", "8", "\"For your lawn\"", "Decor", 5, 3.00, 5.00, this._sellerID));
        temp.add(new Product("Dresses", "9", "\"For your head\"", "Bedding", 53, 3.00, 5.00, this._sellerID));
        //temp.add(new Product("Shoes", "10", "\"For walking with\"", "Clothing", 13, 15.00, 25.00, this._sellerID));

        int i = 0;
       while(i < 3 && prepopCount < 10)
       {
           this._items.addItem(temp.get(prepopCount++));
           i++;
       }

    }

    public double get_costs() {return _costs;}

    public double get_profits() {return _profits;}

    public double get_revenues() {return _revenues;}

    public String get_sellerID() {return _sellerID;}

    public String getUsername() { return super._username;}

    public String getPassword() { return super._password;}

    public User getAccountType() { return super._accountType;}

    public Iterator get_InventoryIterator() { return _items.iterator();}

    public void set_costs(double _costs) {this._costs = _costs;}

    public void set_profits(double _profits) {this._profits = _profits;}

    public void set_revenues(double _revenues) {this._revenues = _revenues;}

    public void set_sellerID(String _sellerID) {this._sellerID = _sellerID;}

    public void setUsername(String username) { super._username = username;}

    public void setPassword(String password) { super._password = password;}

    @Override
    public void update(double sellingPrice, double cost) {
        this._revenues += sellingPrice;
        this._costs += cost;
        this._profits += (sellingPrice - cost);
    }


}
