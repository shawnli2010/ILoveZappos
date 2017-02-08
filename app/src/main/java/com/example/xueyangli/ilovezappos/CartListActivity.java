package com.example.xueyangli.ilovezappos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

public class CartListActivity extends AppCompatActivity implements View.OnClickListener{

    ListView listView;
    private ArrayList<Product> dataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dataSource = ((MyApplication) this.getApplication()).getCart();
        cart = ((MyApplication) this.getApplication()).getCart();

        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(new CartListAdapater(this,dataSource));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), CartListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateHotCount(cart.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.cart);
        View menu_hotlist = menuItem.getActionView();

        currentCartSizeTextView = (TextView) menu_hotlist.findViewById(R.id.cart_count);
        updateHotCount(cart.size());


        return true;
    }

    // call the updating code on the main thread,
    // so we can call this asynchronously
    public void updateHotCount(final int new_cart_size) {
        if (currentCartSizeTextView == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_cart_size == 0)
                    currentCartSizeTextView.setVisibility(View.INVISIBLE);
                else {
                    currentCartSizeTextView.setVisibility(View.VISIBLE);
                    currentCartSizeTextView.setText(Integer.toString(new_cart_size));
                }
            }
        });
    }
}
