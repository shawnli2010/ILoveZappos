package com.example.xueyangli.ilovezappos.adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.GeneralUtil;
import com.example.xueyangli.ilovezappos.activity.CartListActivity;
import com.example.xueyangli.ilovezappos.R;
import com.example.xueyangli.ilovezappos.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xueyangli on 2/8/17.
 */

public class CartListAdapater extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Product> mDataSource;

    private GeneralUtil generalUtil;

    public CartListAdapater(Context context, ArrayList<Product> items){
        this.mContext = context;
        this.mDataSource = items;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.generalUtil = new GeneralUtil(mContext);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = convertView;
        if(rowView == null){
            rowView = mInflater.inflate(R.layout.cart_list_item, parent, false);
        }

        TextView brandNameTextView = (TextView) rowView.findViewById(R.id.cart_brand_name);
        TextView productNameTextView = (TextView) rowView.findViewById(R.id.cart_product_name);
        Button removeButton = (Button) rowView.findViewById(R.id.remove_product_button);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.product_icon);


        Product product = mDataSource.get(position);

        brandNameTextView.setText(product.brandName.toUpperCase());
        productNameTextView.setText(product.productName);

        removeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDataSource.remove(position);
                notifyDataSetChanged();
                if(mContext instanceof CartListActivity){
                    TextView textView = ((CartListActivity) mContext).getCurrentCartSizeTextView();
                    generalUtil.updateHotCount(textView,mDataSource.size());
                    if(mDataSource.size() == 0){
                        ((CartListActivity)mContext).setEmptyLabelVisible(true);
                    }
                }
            }
        });
        Picasso.with(mContext).load(product.thumbnailImageUrl).placeholder(R.mipmap.ic_launcher).into(imageView);


        return rowView;
    }
}
