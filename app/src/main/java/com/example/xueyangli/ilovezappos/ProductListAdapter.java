package com.example.xueyangli.ilovezappos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.model.Product;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xueyangli on 2/6/17.
 */

public class ProductListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Product> mDataSource;

    public ProductListAdapter(Context context, ArrayList<Product> items){
        this.mContext = context;
        this.mDataSource = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = convertView;
        if(rowView == null){
            rowView = mInflater.inflate(R.layout.product_list_item, parent, false);
        }

        TextView headerTextView = (TextView) rowView.findViewById(R.id.header);
        TextView tttTextView = (TextView) rowView.findViewById(R.id.ttt);

        Product product = mDataSource.get(position);

        headerTextView.setText(product.productName);
        tttTextView.setText(product.price);
        return rowView;
    }
}
