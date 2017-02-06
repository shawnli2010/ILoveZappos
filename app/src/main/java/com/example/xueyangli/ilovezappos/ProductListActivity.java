package com.example.xueyangli.ilovezappos;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by xueyangli on 2/6/17.
 */

public class ProductListActivity extends ListActivity {

    ListView listView;
    private ArrayList<Product> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        listView = getListView();
        dataSource = (ArrayList<Product>) getIntent().getSerializableExtra("productListDataSource");
        listView.setAdapter(new ProductAdapter(this,dataSource));
    }
}
