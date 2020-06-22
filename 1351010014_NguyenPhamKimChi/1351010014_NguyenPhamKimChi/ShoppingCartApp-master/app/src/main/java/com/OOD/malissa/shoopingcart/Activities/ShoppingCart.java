package com.OOD.malissa.shoopingcart.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.OOD.malissa.shoopingcart.Controllers.BuyerClerk;
import com.OOD.malissa.shoopingcart.Controllers.SellerClerk;
import com.OOD.malissa.shoopingcart.Controllers.StoreClerk;
import com.OOD.malissa.shoopingcart.Models.Product;
import com.OOD.malissa.shoopingcart.R;

import java.util.ArrayList;

public class ShoppingCart extends Activity {

    private ArrayList<Product> _selectedProducts;
    private ListView _listview;
    private Button _payBtn;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShoppingCart.context = getApplicationContext();

        getProducts();

        setupView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            BuyerClerk.getInstance().reset();
            SellerClerk.getInstance().reset();
            StoreClerk.getInstance().reset();

            Intent i = new Intent(getApplicationContext(), Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public static Context getAppContext() {
        return ShoppingCart.context;
    }

    public void getProducts(){
        BuyerClerk Clerk = BuyerClerk.getInstance();

        _selectedProducts = Clerk.getCartItems();
    }

    private void setupView(){

        setContentView(R.layout.shopping_cart);

        setUpListeners();

    }

    private void setUpListeners(){
        _listview=(ListView)findViewById(R.id.cartProds);
        ShoppingCartAdapter cus = new ShoppingCartAdapter(ShoppingCart.context,_selectedProducts);
        _listview.setAdapter(cus);

        _payBtn = (Button) findViewById(R.id.paybtn);

        _payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_selectedProducts.isEmpty()) {
                    Toast.makeText(getAppContext(), "Shopping Cart is empty. Nothing to purchase.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    BuyerClerk.getInstance().getVerifyPurchase();
                }
            }
        });



    }
}
