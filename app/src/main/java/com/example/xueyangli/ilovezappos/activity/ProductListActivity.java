package com.example.xueyangli.ilovezappos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.GeneralUtil;
import com.example.xueyangli.ilovezappos.MyApplication;
import com.example.xueyangli.ilovezappos.R;
import com.example.xueyangli.ilovezappos.adapater.ProductListAdapter;
import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

/**
 * Product List that will show a list of product from the GET query made to Zappos API
 *
 * Created by xueyangli on 2/6/17.
 */

public class ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    private ArrayList<Product> dataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    private GeneralUtil generalUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        generalUtil = new GeneralUtil(this);
        generalUtil.setActionBarColor();

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
    }

    /*
     * Set up the action bar, update the shopping cart count
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.cart);
        View menu_hotlist = menuItem.getActionView();

        menu_hotlist.setOnClickListener(this);

        currentCartSizeTextView = (TextView) menu_hotlist.findViewById(R.id.cart_count);
        generalUtil.updateHotCount(currentCartSizeTextView,cart.size());


        return true;
    }
}
