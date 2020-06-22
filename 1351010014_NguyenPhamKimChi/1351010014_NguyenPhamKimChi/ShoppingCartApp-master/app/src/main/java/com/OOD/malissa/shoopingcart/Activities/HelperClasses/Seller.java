package com.OOD.malissa.shoopingcart.Activities.HelperClasses;

import android.content.Intent;

import com.OOD.malissa.shoopingcart.Activities.BrowseList;
import com.OOD.malissa.shoopingcart.Activities.Interfaces.UserType;
import com.OOD.malissa.shoopingcart.Activities.Login;


public class Seller implements UserType {

    @Override
    public void setUp(User userEnum) {
        Intent i = new Intent(Login.getAppContext(), BrowseList.class);
        i.putExtra("User", userEnum);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Login.getAppContext().startActivity(i);

    }
}
