package com.OOD.malissa.shoopingcart.Models;

import com.OOD.malissa.shoopingcart.Activities.Interfaces.CartObserver;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cart {

    ArrayList<Product> selectedItems;
    ArrayList<Integer> itemCounts;
    CartObserver browseList;
    private DecimalFormat df = new DecimalFormat("0.00");

    public Cart(){
        selectedItems = new ArrayList<Product>();
        itemCounts = new ArrayList<Integer>();
    }

    public void setObserver(CartObserver cartOb)
    {
        browseList = cartOb;
    }


    public void addItem(Product item){
        if(selectedItems.contains(item)){
            int index = selectedItems.indexOf(item);
            if(itemCounts.get(index) < selectedItems.get(index).get_quantity()) {
            itemCounts.set(index, (itemCounts.get(index)+1));
                if(browseList != null)
                    browseList.update(1,false);
            }
            else
            {if(browseList != null) browseList.update(0,true);}

            return;
        }
        selectedItems.add(item);
        itemCounts.add(1);
        if(browseList != null)
            browseList.update(1,false);

    }

    public void removeItem(Product item){
        if(browseList != null)
            browseList.update(-(itemCounts.get(selectedItems.indexOf(item))),false);

        itemCounts.remove(selectedItems.indexOf(item));
        selectedItems.remove(item);

    }

    public void updateCount(Product item, Integer count){
        Integer cartCount =itemCounts.get(selectedItems.indexOf(item));
        itemCounts.set(selectedItems.indexOf(item), count);
        if(browseList != null)
            browseList.update(count - cartCount, false);

    }

    public Product getCartItems(int index){
     return selectedItems.get(index);
    }

    public int getCartQuantity(){

        return selectedItems.size();
    }

    public int itemCount(Product item) {
        return itemCounts.get(selectedItems.indexOf(item));

    }

    public ArrayList<String> printReceipt() {
        String receiptNames = "";
        String receiptPrices = "";
        float total = 0;

        for(int i = 0; i < selectedItems.size(); i++){
            receiptNames += selectedItems.get(i).get_name() + "\t\t\t\t\t\t\n";
            receiptPrices += "$" + df.format(selectedItems.get(i).get_sellingP()) + " x " + itemCounts.get(i).toString();
            receiptPrices += " = " + "$" + df.format((selectedItems.get(i).get_sellingP()*itemCounts.get(i))) + "\n";

            total += selectedItems.get(i).get_sellingP()*itemCounts.get(i);
        }

        receiptPrices += "\nTotal = " + "$" + df.format(total);

        ArrayList<String> receipt = new ArrayList<>();
        receipt.add(receiptNames);
        receipt.add(receiptPrices);

        return receipt;
    }

    public Product getFirstProd(){

        if(selectedItems.size() > 0) {
            Product first = selectedItems.get(0);
            selectedItems.remove(0);
            return first;
        }

        return null;
    }

    public int getFirstCount(){

        if(itemCounts.size() > 0) {
            int first = itemCounts.get(0);
            itemCounts.remove(0);
            return first;
        }

        return 0;
    }

    public boolean isEmpty(){
        return (selectedItems.isEmpty() && itemCounts.isEmpty());
    }

    public int getCount() {
        int total = 0;
        for(int num : itemCounts )
        {
            total += num;
        }
        return total;
    }
}
