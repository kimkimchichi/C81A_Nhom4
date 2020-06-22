package com.OOD.malissa.shoopingcart.Models;

import com.OOD.malissa.shoopingcart.Models.HelperClasses.IDSellerNDate;
import com.OOD.malissa.shoopingcart.Models.Interfaces.IDAlgorithm;
import com.OOD.malissa.shoopingcart.Models.Interfaces.priceObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Inventory implements Serializable , Iterable {

    private ArrayList<Product> _productList;
    private ArrayList<String> _productNumList;
    private IDAlgorithm _productIDAlgo;
    private priceObserver financeWatcher;
    public Inventory(){

        this._productList = new ArrayList<>();
        this._productNumList = new ArrayList<>();
        this._productIDAlgo = new IDSellerNDate();
    }

    public void setPriceObserver(priceObserver sellerObserver) {this.financeWatcher = sellerObserver;}

    @Override
    public Iterator iterator() {
        return new InventoryIterator();
    }

    public String getNewProductID(String sellerID)
    {
        boolean isUnique;
        String newID;
        do {
            isUnique = true;
            newID = this._productIDAlgo.calculate(sellerID);
            for (String id : _productNumList) {
                if(id.equals(newID))
                {
                    isUnique = false;
                }
            }
        }while(!isUnique);

        return newID;
    }

    public void addItem(Product item)
    {
        this._productList.add(item);
        this._productNumList.add(item.get_ID());
        notifyPriceObserver(0.0,item.get_invoiceP()*item.get_quantity());

    }

    private void notifyPriceObserver(double revenue, double cost)
    {
        if(financeWatcher != null)
            this.financeWatcher.update(revenue,cost);
    }

    public void removeItem(Product item){

        _productList.remove(item);
        System.out.print("item removed");
        notifyPriceObserver(0.0, -(item.get_invoiceP() *item.get_quantity()) );

    }

    private class InventoryIterator implements Iterator {
        int index;
        public InventoryIterator()
        {
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            if(index < _productList.size())
                return true;
            return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return _productList.get(this.index++);
            }
            throw new NoSuchElementException("There are no more elements to get");
        }

        @Override
        public void remove() {
            _productList.remove(--this.index);
            System.out.print("item removed");

        }

    }
}
