package com.example.xueyangli.ilovezappos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.GeneralUtil;
import com.example.xueyangli.ilovezappos.MyApplication;
import com.example.xueyangli.ilovezappos.R;
import com.example.xueyangli.ilovezappos.adapater.CartListAdapater;
import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

/**
 * Shopping cart page, which is also a list of products
 *
 * Created by xueyangli on 2/6/17.
 */

public class CartListActivity extends AppCompatActivity implements View.OnClickListener{

    ListView listView;
    private ArrayList<Product> dataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    private TextView empty_cart;

    private GeneralUtil generalUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Create util instance and set the actionbar's color
        generalUtil = new GeneralUtil(this);
        generalUtil.setActionBarColor();

        // Get the dataSource for the listView
        dataSource = ((MyApplication) this.getApplication()).getCart();
        cart = dataSource;

        // Set adapter for the listView
        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(new CartListAdapater(this,dataSource));

        empty_cart = (TextView) findViewById(R.id.empty_label);
    }

    /*
     * onClick method listens to the actionbar, it will pop the CartListActivty when the
     * actionbar is clicked
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), CartListActivity.class);
        startActivity(intent);
    }

    /*
     * shopping cart size indicator needs to be updated every time the activity re-appear
     */
    @Override
    protected void onStart() {
        super.onStart();
        generalUtil.updateHotCount(currentCartSizeTextView,cart.size());
        if(cart.size() == 0) setEmptyLabelVisible(true);
        else                 setEmptyLabelVisible(false);
    }

    /*
     * Set up the action bar, update the shopping cart count
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.cart);
        View menu_hotlist = menuItem.getActionView();

        currentCartSizeTextView = (TextView) menu_hotlist.findViewById(R.id.cart_count);
        generalUtil.updateHotCount(currentCartSizeTextView,cart.size());

        return true;
    }

    /*
     * setEmptyLabelVisible will determine whether to hide the empty cart message or not
     */
    public void setEmptyLabelVisible(boolean visible){
        if(visible) empty_cart.setVisibility(View.VISIBLE);
        else        empty_cart.setVisibility(View.GONE);
    }

    public TextView getCurrentCartSizeTextView(){
        return currentCartSizeTextView;
    }
}
