package com.OOD.malissa.shoopingcart.Models;

import android.content.Context;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Controllers.StorageController;
import com.OOD.malissa.shoopingcart.Models.Interfaces.Initializable;
import com.OOD.malissa.shoopingcart.Models.Interfaces.Resettable;
import com.OOD.malissa.shoopingcart.Models.Interfaces.Saveable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AccountList implements Iterable, Initializable,Saveable,Resettable {

    private static AccountList ourInstance = new AccountList();

    public static AccountList getInstance() {
        return ourInstance;
    }


    private AccountList() {
        _buyerAccounts = null;
        _sellerAccounts = null;
        _isLookingForSeller = false;
        _isreset = false;
    }
    final private int MAX_BACCOUNT_NUM =  10;
    final private int MAX_SACCOUNT_NUM =  3;

    ArrayList<BuyerAccount> _buyerAccounts;
    ArrayList<SellerAccount> _sellerAccounts;
    boolean _isLookingForSeller;
    boolean _isreset;
    @Override
    public void reset() {
        _buyerAccounts = null;
        _sellerAccounts = null;
        _isLookingForSeller = false;
        _isreset = true;

    }

    @Override
    public Iterator iterator() {
        return new AccountListIterator();
    }

    public Iterator iterator(boolean isSeller) {
        _isLookingForSeller = isSeller;
        return new AccountListIterator();
    }

    @Override
    public void initialized(Object object, String key) {
        _isreset = false;

        if(object == null)
        {

            createPremadeAccounts(key);

        }
        else
        {
            if(key.equals("BuyerList"))
            {
                this._buyerAccounts = (ArrayList<BuyerAccount>) object;
            }
            else if (key.equals("SellerList"))// if it's a seller...
            {
                this._sellerAccounts = (ArrayList<SellerAccount>) object;
            }
            else
            {
               System.out.println("Invalid key used.") ;
            }

        }
    }

    public void addAccount(User user, Account account)
    {
        if(user == User.BUYER)
        {
            this._buyerAccounts.add((BuyerAccount) account);
        }
        else if(user == User.SELLER)
        {
            this._sellerAccounts.add((SellerAccount) account);
        }
    }

    @Override
    public void save(String key,File file,Context context) {

        if(key.equals("BuyerList")) {
            try {
                StorageController.writeObject(context, file, this._buyerAccounts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(key.equals(("SellerList")))
        {
            try {
                StorageController.writeObject(context, file, this._sellerAccounts);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createPremadeAccounts(String key){

        if(key.equals("BuyerList"))
        {
            int count = 0;
            this._buyerAccounts = new ArrayList<>();
            for(int i= 0; i < MAX_BACCOUNT_NUM; i++)
            {
                BuyerAccount buyer = new BuyerAccount("bUser"+i,"123abc"+i);

                if(count % 3 == 0)
                {
                    buyer.addcCard("10001234567891"+ i,"01/12/18");
                }
                this._buyerAccounts.add(buyer);
                count++;
            }


        }
        else if (key.equals("SellerList"))
        {
            this._sellerAccounts = new ArrayList<>();
            for(int i= 0; i < MAX_SACCOUNT_NUM; i++)
            {

                SellerAccount seller = new SellerAccount("sUser"+i,"s123abc"+i);

                seller.prepopulateInventory();
                this._sellerAccounts.add(seller);
            }

        }
        else
        {
            System.out.println("Invalid key used.") ;
        }

    }

    public void set_isLookingForSeller(boolean _isSeller) {
        this._isLookingForSeller = _isSeller;
    }

    public boolean isReset(){ return this._isreset;}



    private class AccountListIterator implements Iterator {

        int index;

        public AccountListIterator()
        {
            index =0;
        }

        @Override
        public boolean hasNext() {

            if(_isLookingForSeller && index < _sellerAccounts.size())
            {
                return true;
            }
            else if(!_isLookingForSeller && index < _buyerAccounts.size())
            {
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(hasNext())
            {
                if(_isLookingForSeller)
                    return _sellerAccounts.get(index++);
                else
                    return _buyerAccounts.get(index++);
            }
            throw new NoSuchElementException("There are no more elements to get");

        }

        @Override
        public void remove() {
            if(_isLookingForSeller)
                 _sellerAccounts.remove(--index);
            else
                 _buyerAccounts.remove(--index);
            if(index < 0) index  = 0;

        }

    }
}
