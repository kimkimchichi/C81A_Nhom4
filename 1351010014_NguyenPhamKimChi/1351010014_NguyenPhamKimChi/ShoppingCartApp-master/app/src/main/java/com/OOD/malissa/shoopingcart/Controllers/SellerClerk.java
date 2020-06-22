package com.OOD.malissa.shoopingcart.Controllers;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.OOD.malissa.shoopingcart.Activities.AddProduct;
import com.OOD.malissa.shoopingcart.Activities.BrowseList;
import com.OOD.malissa.shoopingcart.Activities.HelperClasses.FinancialDialog;
import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Activities.HelperClasses.removeProductDialog;
import com.OOD.malissa.shoopingcart.Models.Product;
import com.OOD.malissa.shoopingcart.Models.SellerAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


public class SellerClerk extends StoreClerk {
    private Iterator _inventoryIter;
    private DecimalFormat df = new DecimalFormat("#.00");
    private static SellerClerk ourInstance = new SellerClerk();

    public static SellerClerk getInstance() {
        return ourInstance;
    }

    private SellerClerk() {
        super();
    }
    protected void setUpUser(User user, SellerAccount seller)
    {
        super._user = user;
        super._userAccountS = seller;
    }

    public void removeProduct(ArrayList<String> infoList){

        boolean foundProduct = false;
        Product newItem = new Product(infoList.get(0),
                infoList.get(1),
                infoList.get(2),
                infoList.get(3),
                Integer.parseInt(infoList.get(4)),
                Double.parseDouble(infoList.get(5)),
                Double.parseDouble(infoList.get(6)),
                infoList.get(7)
        );
        if(_inventoryIter == null)
            _inventoryIter = super._userAccountS.get_InventoryIterator();
        while(_inventoryIter.hasNext() && foundProduct == false)
        {
            Product item = (Product) _inventoryIter.next();
            if(item.equals(newItem))
            {
                super._userAccountS.removeItem(item);
                foundProduct = true;

                this.updateStorage("SellerList");
            }

        }
        if(foundProduct == false)
        {
            Logger log = Logger.getAnonymousLogger();
            log.warning("seller Product is not found in seller inventory ");
        }
        // reset inventory interator
        _inventoryIter = null;
    }

    public void addProduct(){

        Intent i = new Intent(BrowseList.getAppContext(), AddProduct.class);
        super.goToActivity(BrowseList.getAppContext(),i);

    }

    public void saveNewProduct(ArrayList<String> infoList){
        String sellerID = super._userAccountS.get_sellerID();

        String newProductID = super._userAccountS.calculateProductID();
        Product newItem = new Product(infoList.get(0),
                newProductID,
                infoList.get(1),
                infoList.get(2),
                Integer.parseInt(infoList.get(3)),
                Double.parseDouble(infoList.get(4)),
                Double.parseDouble(infoList.get(5)),
                sellerID
        );

        super._userAccountS.addProduct(newItem);

        this.updateStorage("SellerList");

        Intent i = new Intent(AddProduct.getAppContext(), BrowseList.class);
        i.putExtra("User", this._user);
        super.goToActivity(AddProduct.getAppContext(),i);

    }

    public DialogFragment getRemoveProductDialog(ArrayList<String> productInfo){
        DialogFragment dialog = new removeProductDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("product",productInfo);
        args.putString("title", "Remove Product");
        args.putString("message", "Are you sure you want to remove this product from your inventory?");
        dialog.setArguments(args);
        return dialog;

    }


    public DialogFragment getFinanceDialog(){
        DialogFragment dialog = new FinancialDialog();
        Bundle args = new Bundle();
        args.putString("title", "Financial Summary");
        args.putString("message", this._userAccountS.getFinances());
        dialog.setArguments(args);
        return dialog;

    }

    public void saveProductChanges(ArrayList<String> infoList){

        boolean foundProduct = false;
        Product newItem = new Product(infoList.get(0),
                infoList.get(1),
                infoList.get(2),
                infoList.get(3),
                Integer.parseInt(infoList.get(4)),
                Double.parseDouble(infoList.get(5)),
                Double.parseDouble(infoList.get(6)),
                infoList.get(7)
        );
        if(_inventoryIter == null)
            _inventoryIter = super._userAccountS.get_InventoryIterator();
        while(_inventoryIter.hasNext() && foundProduct == false)
        {
            Product item = (Product) _inventoryIter.next();
            if(item.equals(newItem))
            {
                this._userAccountS.update(0.0,(newItem.get_invoiceP()*newItem.get_quantity())
                                                    - (item.get_invoiceP()*item.get_quantity()));

                item.copyData(newItem);
                foundProduct = true;

                this.updateStorage("SellerList");
            }

        }
        if(foundProduct == false)
        {
           Logger log = Logger.getAnonymousLogger();
            log.warning("seller Product is not found in seller inventory ");
        }

        _inventoryIter = null;

    }

    public void returnToBrowseList(Context context)
    {
        Intent i = new Intent(context, BrowseList.class);
        i.putExtra("User", this._user);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public Product getInventoryProd(){
        if(_inventoryIter == null)
            _inventoryIter = super._userAccountS.get_InventoryIterator();
        try {
            return (Product) _inventoryIter.next();
        }
        catch(NoSuchElementException e)
        {
            _inventoryIter = null;
            return null;
        }

    }


}
