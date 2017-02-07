package com.example.xueyangli.ilovezappos;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

/**
 * Created by xueyangli on 2/6/17.
 */

public class ProductListActivity extends AppCompatActivity {

    ListView listView;
    private ArrayList<Product> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
