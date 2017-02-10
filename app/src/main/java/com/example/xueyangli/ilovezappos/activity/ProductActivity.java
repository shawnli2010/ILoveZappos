package com.example.xueyangli.ilovezappos.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.GeneralUtil;
import com.example.xueyangli.ilovezappos.MyApplication;
import com.example.xueyangli.ilovezappos.R;
import com.example.xueyangli.ilovezappos.databinding.ActivityProductBinding;
import com.example.xueyangli.ilovezappos.model.Product;
import com.example.xueyangli.ilovezappos.model.ScaleImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Product Detail Page
 *
 * Created by xueyangli on 2/6/17.
 */

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;
    private ScaleImageView productImageView;
    private ImageView smallProductImageView;
    private ImageButton addToCartButton;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    private GeneralUtil generalUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up action bar
        generalUtil = new GeneralUtil(this);
        generalUtil.setActionBarColor();

        // Get global cart variable
        cart = ((MyApplication) this.getApplication()).getCart();

        // Get the product object that this activity will show
        product = (Product)getIntent().getSerializableExtra("product");

        // Set up data-binding with the XML View
        ActivityProductBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product);
        binding.setProduct(product);

        setUpImageViews();
        setUpTextViews();

        addToCartButton = (ImageButton)findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        doAnimation();
                        cart.add(product);
                        generalUtil.updateHotCount(currentCartSizeTextView,cart.size());
                    }
                }
        );
    }

    /*
     * doAnimation will pop a small picture of the product;
     * move and spin it into the shopping cart
     */
    private void doAnimation(){
        int[] location = new int[2];
        addToCartButton.getLocationOnScreen(location);
        smallProductImageView.setX(location[0] + 50);
        smallProductImageView.setY(location[1] - 300);

        smallProductImageView.setVisibility(View.VISIBLE);
        smallProductImageView.animate().rotationBy(-360)
                                       .translationX(600)
                                       .translationY(-150)
                                       .setDuration(900);
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
        getMenuInflater().inflate(R.menu.my_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.cart);
        View menu_hotlist = menuItem.getActionView();

        menu_hotlist.setOnClickListener(this);

        currentCartSizeTextView = (TextView) menu_hotlist.findViewById(R.id.cart_count);
        generalUtil.updateHotCount(currentCartSizeTextView, cart.size());


        return true;
    }

    private void setUpImageViews(){
        productImageView = (ScaleImageView)findViewById(R.id.product_image);

        smallProductImageView = new ImageView(this);
        RelativeLayout root = (RelativeLayout)findViewById(R.id.activity_product);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        smallProductImageView.setLayoutParams(layoutParams);
        smallProductImageView.setVisibility(View.INVISIBLE);
        root.addView(smallProductImageView);
        setContentView(root);

        Picasso.with(this).load(product.thumbnailImageUrl).placeholder(R.mipmap.ic_launcher).into(productImageView);
        Picasso.with(this).load(product.thumbnailImageUrl).placeholder(R.mipmap.ic_launcher).into(smallProductImageView);
    }

    private void setUpTextViews(){
        TextView original_price = (TextView)findViewById(R.id.original_price);
        original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        TextView percent_off = (TextView)findViewById(R.id.percentOff);
        percent_off.setTextColor(Color.RED);

        original_price.setVisibility(View.INVISIBLE);
        percent_off.setVisibility(View.INVISIBLE);

        if(!product.price.equals(product.originalPrice)){
            original_price.setVisibility(View.VISIBLE);
            percent_off.setVisibility(View.VISIBLE);
        }
    }
}
