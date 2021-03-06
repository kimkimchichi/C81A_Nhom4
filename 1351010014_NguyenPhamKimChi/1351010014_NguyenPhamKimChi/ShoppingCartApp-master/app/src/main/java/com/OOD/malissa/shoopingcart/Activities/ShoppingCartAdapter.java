package com.OOD.malissa.shoopingcart.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.OOD.malissa.shoopingcart.Controllers.BuyerClerk;
import com.OOD.malissa.shoopingcart.Models.Product;
import com.OOD.malissa.shoopingcart.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingCartAdapter extends BaseAdapter {

    LayoutInflater mInlfater;
    ArrayList<Product> list;
    private DecimalFormat df = new DecimalFormat("0.00");

    public ShoppingCartAdapter(Context context,ArrayList<Product> list)
    {
        mInlfater = LayoutInflater.from(context);
        this.list =list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView ==null)
        {
           convertView = mInlfater.inflate(R.layout.custom_list_shopping_cart, parent, false);

           holder = new ViewHolder();
           holder._tv = (TextView) convertView.findViewById(R.id.product_scname);

           holder._price = (TextView) convertView.findViewById(R.id.pricing);

           holder._amount = (Spinner) convertView.findViewById(R.id.amount_spinner);

           holder._removeItem = (Button) convertView.findViewById(R.id.remove_item);

           convertView.setTag(holder);
        }

        else
        {
            holder =(ViewHolder) convertView.getTag();
        }

        Product item = list.get(position);
            holder._tv.setText(item.get_name());

            holder._price.setText("$" + df.format(item.get_sellingP()) + " ea. ");

            ArrayList<Integer> amounts = new ArrayList<Integer>();
            for(int i = 0; i < list.get(position).get_quantity(); i++) {
                amounts.add(i+1);
            }

            ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>
                    (mInlfater.getContext(), R.layout.custom_spinner_item, amounts);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder._amount.setAdapter(spinnerAdapter);
            holder._amount.setSelection((BuyerClerk.getInstance().getItemCount(list.get(position)))-1);
            holder._amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
                    int quantity = (int) parent.getItemAtPosition(itemPosition);
                    BuyerClerk.getInstance().updateCartCount(list.get(position), quantity);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            holder._removeItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    BuyerClerk.getInstance().removeFromCart(list.get(position));
                    list.remove(position);
                    notifyDataSetChanged();

                }
            });

        return convertView;
    }

    static class ViewHolder
    {
        TextView _tv;
        TextView _price;
        Button _removeItem;
        Spinner _amount;
    }

}


