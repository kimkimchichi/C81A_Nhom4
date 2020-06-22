package com.OOD.malissa.shoopingcart.Models.HelperClasses;

import com.OOD.malissa.shoopingcart.Models.Interfaces.IDAlgorithm;

public class IDSellerName implements IDAlgorithm {

    @Override
    public String calculate(String key) {
        String sellerID = "";
        try {
            for (int i = 0; i < key.length(); i++) {
                char letter = key.charAt(i);
                sellerID += Character.getNumericValue(letter);

            }
        }
        catch( IllegalStateException e)
        {
            System.out.println("There is an issue creating the sellerID of: " + key);
            e.printStackTrace();
        }

        return sellerID;
    }
}
