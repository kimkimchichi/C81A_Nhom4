package com.OOD.malissa.shoopingcart.Models.HelperClasses;

import com.OOD.malissa.shoopingcart.Models.Interfaces.IDAlgorithm;

import java.util.Date;

public class IDSellerNDate implements IDAlgorithm {

    @Override
    public String calculate(String key) {
        Integer i = new Integer((int) (new Date().getTime()/1000));
        String newID = key.concat(i.toString());

        return newID;
    }
}
