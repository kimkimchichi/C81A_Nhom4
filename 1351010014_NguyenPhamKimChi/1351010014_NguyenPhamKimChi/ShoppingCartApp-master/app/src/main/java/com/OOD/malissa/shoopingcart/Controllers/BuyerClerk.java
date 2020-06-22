package com.OOD.malissa.shoopingcart.Controllers;

import android.content.Context;
import android.content.Intent;

import com.OOD.malissa.shoopingcart.Activities.BrowseList;
import com.OOD.malissa.shoopingcart.Activities.Checkout;
import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Activities.Interfaces.CartObserver;
import com.OOD.malissa.shoopingcart.Activities.Payment;
import com.OOD.malissa.shoopingcart.Activities.ShoppingCart;
import com.OOD.malissa.shoopingcart.Models.AccountList;
import com.OOD.malissa.shoopingcart.Models.BuyerAccount;
import com.OOD.malissa.shoopingcart.Models.Cart;
import com.OOD.malissa.shoopingcart.Models.CreditCard;
import com.OOD.malissa.shoopingcart.Models.Product;
import com.OOD.malissa.shoopingcart.Models.SellerAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BuyerClerk extends StoreClerk {


    private Cart _shoppingCart;
    private Iterator _currentInventoryIter;
    private Iterator _sellerIterator;
    private DecimalFormat df = new DecimalFormat("0.00");

    private static BuyerClerk ourInstance = new BuyerClerk();

    public static BuyerClerk getInstance() {
        return ourInstance;
    }

    private BuyerClerk() {
        super();
        _shoppingCart = null;
        _currentInventoryIter = null;
        _sellerIterator = null;
    }
    @Override
    public void reset() {

        if(this._accList != null && !this._accList.isReset())
        {
            this._accList.reset();
        }

        this._shoppingCart = null;
        this._user = null;
        this._userAccountB = null;
        this._userAccountS = null;
        this._currentInventory = null;
    }

    public void addToCart(Product item){
        _shoppingCart.addItem(item);

    }

    public void addToCart(String productID, String SellerID) {
        boolean foundProduct = false;

        if (_currentInventoryIter == null) {
            setNextInventory();
        }
        while(foundProduct == false) {
            while (_currentInventoryIter.hasNext() && foundProduct == false) {

                Product item = (Product) _currentInventoryIter.next();
                if(item.equals(productID,SellerID))
                {
                    _shoppingCart.addItem(item);
                    foundProduct = true;
                }
            }
            try {
                if(!_currentInventoryIter.hasNext() && foundProduct == false) {
                    _currentInventoryIter = null;
                    setNextInventory();
                }

            }
            catch(NoSuchElementException e)
            {
                _currentInventoryIter = null;
                _sellerIterator = null;
                System.out.println("Could not find product in inventory ");
                e.printStackTrace();

            }
        }
        _currentInventoryIter = null;
        _sellerIterator = null;


    }

    public void removeFromCart(Product item){
        _shoppingCart.removeItem(item);
        }

    public void showShoppingCart(){
        Intent i = new Intent(BrowseList.getAppContext(), ShoppingCart.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BrowseList.getAppContext().startActivity(i);
    }

    public void updateCartCount(Product item, int quantity){
        _shoppingCart.updateCount(item, quantity);
    }

    public  int getCartCount(){
        if(_shoppingCart != null)
            return _shoppingCart.getCount();
        return 0;
   }

    public int getItemCount(Product item) {
        return _shoppingCart.itemCount(item);
    }

    public void finalCheckout(){
        Intent i = new Intent(Payment.getAppContext(), Checkout.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Payment.getAppContext().startActivity(i);

    }

    public ArrayList<String> getCreditInfo() {
        ArrayList<String> accountNumbers = new ArrayList<String>();
        ArrayList<CreditCard> cCards = StoreClerk.getInstance()._userAccountB.getcCards();
        for(int i = 0; i < cCards.size(); i++) {
            accountNumbers.add(cCards.get(i).getAccNumber());
        }
        return accountNumbers;
    }

    public void addNewCredit(String accNum, String expiration){
        StoreClerk.getInstance()._userAccountB.addcCard(accNum, expiration);
    }

    public void getVerifyPurchase(){
        Intent i = new Intent(ShoppingCart.getAppContext(), Payment.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ShoppingCart.getAppContext().startActivity(i);

    }

    public void paySeller(){
        while(!_shoppingCart.isEmpty()) {
            Product prod = _shoppingCart.getFirstProd();
            int count = _shoppingCart.getFirstCount();

            AccountList accList = StoreClerk.getInstance()._accList;

            accList.set_isLookingForSeller(true);

            for (Iterator iter = _accList.iterator(); iter.hasNext(); ) {
                SellerAccount seller = (SellerAccount) iter.next();

                if (prod.get_SellerID().equals(seller.get_sellerID())) {
                    seller.update(prod.get_sellingP() * count,0.0);

                    this._userAccountB.setBill(prod.get_sellingP() * count);


                    if((prod.get_quantity() - count)< 0)
                    {
                        throw new IllegalArgumentException("quantity purchased is more than what is there.");
                    }
                    else {
                        prod.set_quantity(prod.get_quantity() - count);
                    }

                    this.updateStorage("SellerList");
                    this.updateStorage("BuyerList");

                }
            }
        }

    }

    public ArrayList<String> getReceipt() {
        return _shoppingCart.printReceipt();
    }



    public void openBrowseList(Context context) {
        Intent i = new Intent(context, BrowseList.class);
        i.putExtra("User", User.BUYER);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public ArrayList<Product> getCartItems(){

        ArrayList<Product> cartItems = new ArrayList<Product>();

        for (int i = 0; i < _shoppingCart.getCartQuantity(); i++) {
            cartItems.add(_shoppingCart.getCartItems(i));
        }

        return cartItems;
    }


    private void setNextInventory(){

        if(_sellerIterator == null)
        {
           _sellerIterator = _accList.iterator(true);
        }


        SellerAccount s = (SellerAccount) _sellerIterator.next();
        _currentInventoryIter = s.get_InventoryIterator();

    }

    public Product getAStoreProd(){

        if (_currentInventoryIter == null) {
            setNextInventory();
        }

        try{
           return (Product)_currentInventoryIter.next();
        }
        catch(NoSuchElementException ex)
        {
            _currentInventoryIter = null;
            try {

                setNextInventory();
                return (Product)_currentInventoryIter.next();
            }
            catch(NoSuchElementException e)
            {
                _currentInventoryIter = null;
                _sellerIterator = null;
                return null;
            }

        }

    }

    public void setUpUser(User user, BuyerAccount buyer) {
        super._user = user;
        super._userAccountB = buyer;

        _shoppingCart = new Cart();

    }

    public void setCartObserver(CartObserver ob)
    {
        _shoppingCart.setObserver(ob);
    }

    public String getBuyerBill() {
        return "$" + df.format(this._userAccountB.getBill());
    }
}
