package com.OOD.malissa.shoopingcart.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Activities.Interfaces.CartObserver;
import com.OOD.malissa.shoopingcart.Controllers.BuyerClerk;
import com.OOD.malissa.shoopingcart.Controllers.SellerClerk;
import com.OOD.malissa.shoopingcart.Controllers.StoreClerk;
import com.OOD.malissa.shoopingcart.Models.Product;
import com.OOD.malissa.shoopingcart.R;

import java.util.ArrayList;

public class BrowseList extends Activity implements CartObserver{


    private ArrayList<Product> _products;
    private ListView _listview;
    private Button _financialBtn;
    private Button _addProdBtn;
    private Button _checkoutBtn;
    private User _currentUser;
    private BuyerClerk bClerk = BuyerClerk.getInstance();
    private SellerClerk sClerk = SellerClerk.getInstance();
    private int currentCartCount;
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _currentUser = (User) getIntent().getSerializableExtra("User");
        BrowseList.context = getApplicationContext();
        if(_currentUser == User.BUYER)
            currentCartCount = bClerk.getCartCount();
        _products = new ArrayList<>();
        getProducts();

        setupView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browse_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            bClerk.reset();
            sClerk.reset();
            StoreClerk.getInstance().reset();

            Intent i = new Intent(getApplicationContext(), Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);

            return true;
        }
        if (id == R.id.accinfo) {
            StoreClerk.getInstance().showAccountInfo(getAppContext(), _currentUser);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void update(int count, boolean isEmpty) {
        currentCartCount += count;
        if(_checkoutBtn != null)
        {
            _checkoutBtn.setText("View Cart("+ currentCartCount+")");
        }
        if(count == 0 && isEmpty)
        {
            Toast.makeText(getApplicationContext(), "no more products to add.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(BrowseList.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            pdLoading.dismiss();
        }

    }

    public static Context getAppContext() {
        return BrowseList.context;
    }

    public void getProducts(){

        if(_currentUser == User.BUYER)
        {
            Product x = null;
            do {
                x = bClerk.getAStoreProd();
                if(x != null)
                {
                    if(x.get_quantity() != 0) {
                        _products.add(x);

                    }
                }
            }while(x != null);
        }
        else if (_currentUser == User.SELLER)
        {
            Product x = null;
            do {
                x = sClerk.getInventoryProd();
                if(x != null)
                {
                    _products.add(x);
                }
            }while(x != null);

        }

    }

    private void setupView(){


        if(_currentUser == User.BUYER){
            setContentView(R.layout.browse_list_buyer);
            bClerk.setCartObserver(this);
        }
        else if(_currentUser == User.SELLER) {
            setContentView(R.layout.browse_list_seller);
        }

        setUpListeners();
    }

    private void setUpListeners(){

        _listview=(ListView)findViewById(R.id.list);
        final BrowseListAdapter cus = new BrowseListAdapter(BrowseList.context,_products, _currentUser);
        _listview.setAdapter(cus);
        _listview.setLongClickable(true);
        _listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
             @Override
             public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
             {
                Product item = (Product) cus.getItem(pos);
                 if(_currentUser == User.BUYER)
                 {
                     bClerk.getProductDets(item);
                 }
                 else if (_currentUser == User.SELLER)
                 {
                     sClerk.getProductDets(item);
                 }

                 return true;
             }
         }
        );
        if(_currentUser == User.BUYER){

            _checkoutBtn = (Button) findViewById(R.id.check_out);

            _checkoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuyerClerk.getInstance().showShoppingCart();
                }
            });
        }
        else if(_currentUser == User.SELLER) {
            _addProdBtn = (Button) findViewById(R.id.new_prod);
            _financialBtn = (Button) findViewById(R.id.fin_sum);

            _addProdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sClerk.addProduct();


                }
            });

            _financialBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogFragment dialog = sClerk.getFinanceDialog();
                    dialog.show(getFragmentManager(), "finance");
                }
            });
        }

        this.update(0,false);

    }


}

