package com.OOD.malissa.shoopingcart.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.OOD.malissa.shoopingcart.Controllers.StoreClerk;
import com.OOD.malissa.shoopingcart.R;

import java.util.ArrayList;

public class Login extends Activity {


    private CheckBox _checkBoxSeller;
    private Button _loginBtn;
    private Button _registerBtn;
    private EditText _userName;
    private EditText _password;
    private StoreClerk Clerk = StoreClerk.getInstance();
    private static Context context;
    private boolean _isInitialized;
    private boolean _isSeller = false;
    private String _usernameString;
    private String _passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _isInitialized = false;
        Login.context = getApplicationContext();


        setContentView(R.layout.login);
        setUpListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!_isInitialized) {
            Clerk.initializeAllModel(context);
            this._isInitialized = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public static Context getAppContext() {
        return Login.context;
    }

    private void setUpListeners(){

        this._loginBtn = (Button)  findViewById(R.id.logInButton);
        this._password = (EditText) findViewById(R.id.passwordField);
        this._userName = (EditText) findViewById(R.id.usernameField);
        this._checkBoxSeller = (CheckBox)  findViewById(R.id.userTypeCheck);
        this._registerBtn = (Button) findViewById(R.id.register);

        this._userName.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                      _usernameString = _userName.getText().toString();
                  }

                  @Override
                  public void afterTextChanged(Editable s) {

                  }
              }

        );

        this._registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clerk.register(_usernameString, _passwordString, _isSeller);
            }
        });

        this._password.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                      _passwordString = _password.getText().toString();
                  }

                  @Override
                  public void afterTextChanged(Editable s) {

                  }
              }

        );

        this._checkBoxSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _isSeller = !_isSeller;
            }
        });

        this._loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           Clerk.login(_usernameString, _passwordString, _isSeller);

            }
        });

    }
}
