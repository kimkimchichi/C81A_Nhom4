package com.OOD.malissa.shoopingcart.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.OOD.malissa.shoopingcart.Controllers.BuyerClerk;
import com.OOD.malissa.shoopingcart.Controllers.SellerClerk;
import com.OOD.malissa.shoopingcart.Controllers.StoreClerk;
import com.OOD.malissa.shoopingcart.R;

import java.util.ArrayList;


public class AddProduct extends Activity {

    private ArrayList<String> _productInfo;
    private ArrayList<EditText> _productTextFields;
    private EditText _prodName;
    private EditText _prodDesc;
    private EditText _prodIPrice;
    private EditText _prodSPrice;
    private EditText _prodQuant;
    private EditText _prodType;
    private Button _cancelBtn;
    private Button _saveBtn;
    private static Context context;
    private SellerClerk sClerk = SellerClerk.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AddProduct.context = getApplicationContext();

        _productInfo = new ArrayList<>();
        _productTextFields = new ArrayList<>();

        setupView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
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

    public static Context getAppContext() {
        return AddProduct.context;
    }

    private void setupView(){

        setContentView(R.layout.add_product);
        setupListeners();
    }

    private void setupListeners(){

        _prodName = (EditText)findViewById(R.id.addName);
         _prodDesc = (EditText)findViewById(R.id.addDeets);
         _prodIPrice = (EditText)findViewById(R.id.addcost);
         _prodSPrice = (EditText)findViewById(R.id.addprice);
        _prodQuant =(EditText)findViewById(R.id.addquant);
         _prodType =(EditText)findViewById(R.id.addType);

        _productTextFields.add(_prodName);
        _productTextFields.add(_prodDesc);
        _productTextFields.add(_prodSPrice);
        _productTextFields.add(_prodQuant);
        _productTextFields.add(_prodIPrice);
        _productTextFields.add(_prodType);

        _cancelBtn = (Button) findViewById(R.id.addCancelbtn);
        _saveBtn = (Button) findViewById(R.id.addProdBtn);



        _cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BrowseList.class);
                i.putExtra("User", sClerk.currentUserType() );
                sClerk.goToActivity(getApplicationContext(),i);
            }
        });

        _saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canSave = true;
                for(EditText text : _productTextFields)
                {

                    if(text.getText().toString().equals(""))
                    {
                        canSave = false;
                        break;
                    }
                }
                if(canSave) {
                    _productInfo.add(_prodName.getText().toString());
                    _productInfo.add(_prodDesc.getText().toString());
                    _productInfo.add(_prodType.getText().toString());
                    _productInfo.add(_prodQuant.getText().toString());
                    _productInfo.add(_prodIPrice.getText().toString());
                    _productInfo.add(_prodSPrice.getText().toString());

                    sClerk.saveNewProduct(_productInfo);

                    Toast.makeText(getAppContext(), "new Product added.",
                            Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill all fields correctly.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
