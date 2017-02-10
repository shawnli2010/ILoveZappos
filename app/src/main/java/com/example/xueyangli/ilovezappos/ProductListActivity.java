package com.example.xueyangli.ilovezappos;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

/**
 * Created by xueyangli on 2/6/17.
 */

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    private ArrayList<Product> dataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#7399C7"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        cart = ((MyApplication) this.getApplication()).getCart();

        dataSource = (ArrayList<Product>) getIntent().getSerializableExtra("productListDataSource");

        listView = (ListView)findViewById(android.R.id.list);
        listView.setAdapter(new ProductListAdapter(this,dataSource));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = dataSource.get(position);
                Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                intent.putExtra("product",product);
                startActivity(intent);
            }
        });
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

        menu_hotlist.setOnClickListener(this);

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
