package com.OOD.malissa.shoopingcart.Controllers;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import com.OOD.malissa.shoopingcart.Activities.Account;
import com.OOD.malissa.shoopingcart.Activities.BrowseList;
import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Activities.Login;
import com.OOD.malissa.shoopingcart.Activities.ProductDetails;
import com.OOD.malissa.shoopingcart.Models.AccountList;
import com.OOD.malissa.shoopingcart.Models.Interfaces.Resettable;
import com.OOD.malissa.shoopingcart.Models.SellerAccount;
import com.OOD.malissa.shoopingcart.Models.BuyerAccount;
import com.OOD.malissa.shoopingcart.Models.Inventory;
import com.OOD.malissa.shoopingcart.Models.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class StoreClerk implements Resettable{


    protected  User _user;
    protected  BuyerAccount _userAccountB;
    protected  SellerAccount _userAccountS;
    protected AccountList _accList;
    protected Inventory _currentInventory;
    private static StoreClerk ourInstance = new StoreClerk();

    public static StoreClerk getInstance() {
        return ourInstance;
    }

    protected StoreClerk() {
        this._accList = AccountList.getInstance();
        this._user = null;
        this._userAccountB = null;
        this._userAccountS = null;
        this._currentInventory = null;
    }
    @Override
    public void reset() {

        if(this._accList != null && !this._accList.isReset())
        {
            this._accList.reset();
        }

        this._user = null;
        this._userAccountB = null;
        this._userAccountS = null;
        this._currentInventory = null;
    }

    public SellerAccount get_userAccountS(){ return this._userAccountS;}

    public BuyerAccount get_userAccountB(){ return this._userAccountB;}

    public void initializeAllModel(Context context){
        String currentStorageKey = null;
        Object savedItem = null;
        String baseFolder = context.getFilesDir().getAbsolutePath();
        File file;
        try {
            currentStorageKey = "BuyerList";

            file = new File(baseFolder + File.separator + currentStorageKey + ".ser");

            if (!file.exists())
                throw new IOException();
                savedItem = StorageController.readObject(context, file);

        } catch ( ClassCastException e) {
            System.out.println("Incorrect object cast for : " + currentStorageKey);
            e.printStackTrace();

        }  catch (IOException e) {
            System.out.println("Issue getting data from: " + currentStorageKey);
            e.printStackTrace();
            savedItem = null;


        } catch (ClassNotFoundException e) {
            System.out.println("The class is not found. Issue getting data from: " + currentStorageKey);
            e.printStackTrace();
        }
        _accList.initialized(savedItem, currentStorageKey);


          try{
            currentStorageKey = "SellerList";
            baseFolder = context.getFilesDir().getAbsolutePath();
            file = new File(baseFolder + File.separator + currentStorageKey + ".ser");

            if (!file.exists())
                throw new IOException();
            savedItem = StorageController.readObject(context,file);


        } catch ( ClassCastException e) {
            System.out.println("Incorrect object cast for : " + currentStorageKey);
            e.printStackTrace();

        }  catch (IOException e) {
            System.out.println("Issue getting data from: " + currentStorageKey);
            e.printStackTrace();
              savedItem = null;


        } catch (ClassNotFoundException e) {
            System.out.println("The class is not found. Issue getting data from: " + currentStorageKey);
            e.printStackTrace();
        }
        _accList.initialized(savedItem, currentStorageKey);



    }

    public void updateStorage(String identifier){

        if(identifier.equals("BuyerList") || identifier.equals("SellerList")) {
            String baseFolder = Login.getAppContext().getFilesDir().getAbsolutePath();
            File file = new File(baseFolder  + File.separator + identifier + ".ser");

            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                _accList.save(identifier,file, Login.getAppContext());

        }


    }

    public boolean verifyAccount(String username, String pass, boolean isSeller){
        _accList.set_isLookingForSeller(isSeller);

        for(Iterator iter = _accList.iterator(); iter.hasNext();) {
            if(isSeller){
                this._userAccountS = (SellerAccount) iter.next();
                if (this._userAccountS.getPassword().equals(pass) && this._userAccountS.getUsername().equals(username))
                    {return true;}
            }
            else {

                this._userAccountB = (BuyerAccount) iter.next();
                if (this._userAccountB.getPassword().equals(pass) && this._userAccountB.getUsername().equals(username))
                    return true;
            }

        }

        return false;
    }

    public void login(String username, String pass, boolean isSeller) {

        if(verifyAccount(username, pass, isSeller)) {

            if(isSeller) {
                _user = User.SELLER;
                SellerClerk.getInstance().setUpUser(this._user,this._userAccountS);
            }
            else {
                _user = User.BUYER;
                BuyerClerk.getInstance().setUpUser(this._user,this._userAccountB);
            }

            setUser();

        }
        else
        {
            Toast.makeText(Login.getAppContext(), "Invalid username or password.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void setUser(){
        Intent i = new Intent(Login.getAppContext(), BrowseList.class);
        i.putExtra("User", this._user);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.getAppContext().startActivity(i);
    }


    public User currentUserType() { return this._user;}

    public void getProductDets(Product item){

        Intent i = new Intent(BrowseList.getAppContext(), ProductDetails.class);
        i.putExtra("User", this._user);
        i.putExtra("Product", item.toArrayList());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        goToActivity(BrowseList.getAppContext(),i);
    }

    public void updateAccount(ArrayList<String> infoListArrayList, User userType){
        if(userType == User.BUYER){
            this._userAccountB.setUsername(infoListArrayList.get(0));
            this._userAccountB.setPassword(infoListArrayList.get(1));
            updateStorage("BuyerList");
        }
        if(userType == User.SELLER){
            this._userAccountS.setUsername(infoListArrayList.get(0));
            this._userAccountS.setPassword(infoListArrayList.get(1));
            updateStorage("SellerList");
        }

    }


    public boolean checkUsername(String username, User userType) {
        boolean isSeller;

        if(userType == User.BUYER){
            isSeller = false;
        }
        else{
            isSeller = true;
        }

        _accList.set_isLookingForSeller(isSeller);

        for(Iterator iter = _accList.iterator(); iter.hasNext();) {
            if(isSeller){
                if(username.equals(((SellerAccount) iter.next()).getUsername()))
                    return false;
            }
            else {
                if(username.equals(((BuyerAccount) iter.next()).getUsername()))
                    return false;
            }
        }
        return true;
    }

    public void showAccountInfo(Context context, User userType){
        Intent i = new Intent(context, Account.class);
        i.putExtra("User", this._user);
        if (userType == User.BUYER) {
            i.putExtra("Account", _userAccountB.toArrayList());
        }
        if (userType == User.SELLER) {
            i.putExtra("Account",_userAccountS.toArrayList());
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BrowseList.getAppContext().startActivity(i);
    }

    public void goToActivity(Context from, Intent i)
    {
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       from.startActivity(i);

    }

    public void register(String usernameString, String passwordString, boolean isSeller) {
        User user;
        if(isSeller)
            user = User.SELLER;
        else
            user = User.BUYER;

        if(usernameString == null|| passwordString == null || !usernameString.matches("\\b\\w+\\b") || !passwordString.matches("\\b\\w+\\b"))
        {
            Toast.makeText(Login.getAppContext(), "Please fill in Username and password with correct values",
                    Toast.LENGTH_LONG).show();
        }
       else if( checkUsername(usernameString, user) )
       {
           if(isSeller)
           {
               SellerAccount account = new SellerAccount(usernameString,passwordString);
               this._accList.addAccount(user,account);
               this.updateStorage("SellerList");

           }
           else
           {
               BuyerAccount account = new BuyerAccount(usernameString,passwordString);
               this._accList.addAccount(user,account);
               this.updateStorage("BuyerList");
           }

           login(usernameString,passwordString,isSeller);

       }
        else
       {
           Toast.makeText(Login.getAppContext(), "Username taken.",
                   Toast.LENGTH_LONG).show();
       }
    }
}
