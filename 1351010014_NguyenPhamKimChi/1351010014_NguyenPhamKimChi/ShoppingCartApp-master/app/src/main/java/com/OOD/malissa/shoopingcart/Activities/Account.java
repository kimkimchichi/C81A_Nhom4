package com.OOD.malissa.shoopingcart.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.OOD.malissa.shoopingcart.Activities.HelperClasses.User;
import com.OOD.malissa.shoopingcart.Controllers.BuyerClerk;
import com.OOD.malissa.shoopingcart.Controllers.SellerClerk;
import com.OOD.malissa.shoopingcart.Controllers.StoreClerk;
import com.OOD.malissa.shoopingcart.R;

import java.util.ArrayList;

public class Account extends Activity {

    //region INSTANCE VARIABLES
    private ArrayList<EditText> _infoTextList = new ArrayList<>();
    private Button _returnBtn;
    private Button _editBtn;
    private Button _cancelBtn;
    private Button _saveBtn;
    private EditText _userName;
    private TextView _bill;
    private EditText _password;
    private TextView _cPassTitle;
    private EditText _cPassword;
    private boolean _uniqueUser = true;
    private boolean _passwordMatch = true;
    private User _currentUser;
    private ArrayList<String> _accountInfo;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Account.context = getApplicationContext();
        _currentUser = (User) getIntent().getSerializableExtra("User");
        _accountInfo = (ArrayList<String>) getIntent().getSerializableExtra("Account");

        setContentView(R.layout.account);
        setUpListeners();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
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
        return Account.context;
    }

    private void setUpListeners(){

        _returnBtn = (Button)  findViewById(R.id.accountReturn);
        _editBtn = (Button)  findViewById(R.id.accountEdit);
        _cancelBtn = (Button)  findViewById(R.id.accountCancel);
        _saveBtn = (Button)  findViewById(R.id.accountSave);
        _userName = (EditText) findViewById(R.id.accountUname);
        _password = (EditText) findViewById(R.id.accountPword);
        _cPassTitle = (TextView) findViewById(R.id.accountCPwordTitle);
        _cPassword = (EditText) findViewById(R.id.accountCPword);
        _bill = (TextView) findViewById(R.id.billText);
        if(_currentUser == User.BUYER) {
            _bill.setText("Bill: " + BuyerClerk.getInstance().getBuyerBill());
        }
        else
        {

            _bill.setVisibility(View.GONE);
        }



        _infoTextList.add(_userName);
        _infoTextList.add(_password);
        _infoTextList.add(_cPassword);

        _userName.setText(_accountInfo.get(0));
        _password.setText(_accountInfo.get(1));
        _cPassword.setText(_accountInfo.get(1));


        _userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            //After losing focus, check if the Username is already taken
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else {
                    if(_userName.getText().toString().equals(_accountInfo.get(0))){
                        _uniqueUser = true;
                    }
                    else {
                        if (StoreClerk.getInstance().checkUsername(_userName.getText().toString(), _currentUser)) {
                            _uniqueUser = true;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Username has already been taken.",
                                    Toast.LENGTH_LONG).show();
                            _uniqueUser = false;
                        }
                    }
                }
            }
        });

        _password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //When the password field's focus is lost, check if confirm password is the same.
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else {
                    if(_password.getText().toString().equals(_cPassword.getText().toString())){
                        _passwordMatch = true;
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password does not match.",
                                Toast.LENGTH_LONG).show();
                        _passwordMatch = false;

                    }
                }
            }
        });

        _cPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //When the confirm password field's focus is lost, check if password is the same.
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }else {
                    if(_password.getText().toString().equals(_cPassword.getText().toString())){
                        _passwordMatch = true;
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password does not match.",
                                Toast.LENGTH_LONG).show();
                        _passwordMatch = false;
                    }
                }
            }
        });

        _returnBtn.setOnClickListener(new View.OnClickListener(){
            //Returns the user to the BrowseList
            @Override
            public void onClick(View v) {
                if(_currentUser == User.BUYER) {
                    finish();
                    BuyerClerk.getInstance().openBrowseList(getAppContext());
                }
                if(_currentUser == User.SELLER) {
                    finish();
                    SellerClerk.getInstance().returnToBrowseList(getAppContext());
                }

            }
        });

        _editBtn.setOnClickListener(new View.OnClickListener() {
            //Sets the screen to be able to edit the account information
            @Override
            public void onClick(View v) {
                _returnBtn.setVisibility(View.GONE);
                _editBtn.setVisibility(View.GONE);
                _cancelBtn.setVisibility(View.VISIBLE);
                _saveBtn.setVisibility(View.VISIBLE);
                _cPassTitle.setVisibility(View.VISIBLE);
                _cPassword.setVisibility(View.VISIBLE);

                for (EditText text : _infoTextList) {
                    text.setEnabled(true);
                    text.setFocusableInTouchMode(true);
                    text.setBackgroundColor(Color.LTGRAY);
                }
                if(_currentUser == User.BUYER) {_bill.setVisibility(View.INVISIBLE);}

            }
        });

        _cancelBtn.setOnClickListener(new View.OnClickListener() {
            //Returns the screen to just show the account information
            @Override
            public void onClick(View v) {
                _returnBtn.setVisibility(View.VISIBLE);
                _editBtn.setVisibility(View.VISIBLE);
                _cancelBtn.setVisibility(View.GONE);
                _saveBtn.setVisibility(View.GONE);
                _cPassTitle.setVisibility(View.GONE);
                _cPassword.setVisibility(View.GONE);


                for (EditText text : _infoTextList) {
                    text.setEnabled(false);
                    text.setFocusableInTouchMode(false);
                    text.setBackgroundColor(Color.TRANSPARENT);
                }

                _userName.setText(_accountInfo.get(0));
                _password.setText(_accountInfo.get(1));
                _cPassword.setText(_accountInfo.get(1));
                if(_currentUser == User.BUYER) {_bill.setVisibility(View.VISIBLE);}

            }
        });


        _saveBtn.setOnClickListener(new View.OnClickListener() {
            //Takes the data from the edit fields and saves only if the username is unique and
            //the password and confirm password are the same. Then returns screen to show account info.
            @Override
            public void onClick(View v) {

                if(_passwordMatch && _uniqueUser) {

                    _returnBtn.setVisibility(View.VISIBLE);
                    _editBtn.setVisibility(View.VISIBLE);
                    _cancelBtn.setVisibility(View.GONE);
                    _saveBtn.setVisibility(View.GONE);
                    _cPassTitle.setVisibility(View.GONE);
                    _cPassword.setVisibility(View.GONE);

                    for (EditText text : _infoTextList) {
                        text.setEnabled(false);
                        text.setFocusableInTouchMode(false);
                        text.setBackgroundColor(Color.TRANSPARENT);
                    }

                    _accountInfo.set(0, _userName.getText().toString());
                    _accountInfo.set(1, _password.getText().toString());

                    StoreClerk.getInstance().updateAccount(_accountInfo, _currentUser);
                }

                else if(_passwordMatch && !_uniqueUser) {
                    Toast.makeText(getApplicationContext(), "Username has already been taken.",
                            Toast.LENGTH_LONG).show();
                }

                else if(!_passwordMatch && _uniqueUser) {
                    Toast.makeText(getApplicationContext(), "Password does not match.",
                            Toast.LENGTH_LONG).show();
                }

                else if(!_passwordMatch && !_uniqueUser) {
                    Toast.makeText(getApplicationContext(), "Username has already been taken and Password does not match.",
                            Toast.LENGTH_LONG).show();
                }

                if(_currentUser == User.BUYER) {_bill.setVisibility(View.VISIBLE);}

            }
        });
    }

}
