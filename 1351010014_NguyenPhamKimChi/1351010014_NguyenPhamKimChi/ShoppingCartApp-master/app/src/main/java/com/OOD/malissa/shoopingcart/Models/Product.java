package com.OOD.malissa.shoopingcart.Models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product  implements Serializable {

    private String _name;
    private String _ID;
    private String _description;
    private String _type;
    private int _quantity;
    private double _invoiceP;
    private double _sellingP;
    private String _SellerID;
    private DecimalFormat df = new DecimalFormat("#.00");

    public Product(){}
    public Product(String name, String id, String descrip, String type, int quantity,
                   double invoice, double selling, String sellerID){
        this._name = name;
        this._ID = id;
        this._description = descrip;
        this._type = type;
        this._quantity = quantity;
        this._invoiceP = invoice;
        this._sellingP = selling;
        this._SellerID = sellerID;
    }

    public String get_name() { return _name;}

    public String get_ID() {return _ID; }

    public String get_description() {return _description;}

    public String get_type() {return _type;}

    public int get_quantity() {return _quantity; }

    public double get_invoiceP() {return _invoiceP;}

    public double get_sellingP() {return _sellingP;}

    public String get_SellerID() {return _SellerID;}

    @Override
    public String toString() {return _name + " $" + df.format(_sellingP);}

    public void set_name(String _name) {this._name = _name; }

    public void set_ID(String _ID) {this._ID = _ID;}

    public void set_description(String _description) {this._description = _description;}

    public void set_type(String _type) {this._type = _type;}

    public void set_quantity(int _quantity) {this._quantity = _quantity;}

    public void set_invoiceP(double _invoiceP) {this._invoiceP = _invoiceP;}

    public void set_sellingP(double _sellingP) {this._sellingP = _sellingP;}

    public void set_SellerID(String _SellerID) {this._SellerID = _SellerID;}

    @Override
    public boolean equals(Object other) {

        if(this == other) return true;
        if(other == null) return false;
        if (getClass() != other.getClass()) return false;

        Product otherProd = (Product) other;


        return (this.get_ID().equals(otherProd.get_ID())
                        && this.get_SellerID().equals(otherProd.get_SellerID()));
    }


    public boolean equals(String otherID,String otherSellerID) {

        if(otherID == null ||otherSellerID == null ) return false;

        return ( this.get_ID().equals(otherID)
                && this.get_SellerID().equals(otherSellerID));
    }


    public ArrayList<String> toArrayList() {

        ArrayList<String> list = new ArrayList<>();
        list.add(this._name);
        list.add(this._ID);
        list.add(this._description);
        list.add(this._type);
        Integer quantity = this._quantity;
        list.add(quantity.toString());
        list.add(df.format(this._invoiceP));
        list.add(df.format(this._sellingP));
        list.add(this._SellerID);


        return list;}

    public void copyData(Product item){

        this._sellingP = item.get_sellingP();
        this._invoiceP = item.get_invoiceP();
        this._description = item.get_description();
        this._name = item.get_name();
        this._quantity = item.get_quantity();
        this._type = item.get_type();

    }

}
