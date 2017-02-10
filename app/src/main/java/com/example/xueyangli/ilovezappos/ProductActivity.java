package com.example.xueyangli.ilovezappos;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.databinding.ActivityProductBinding;
import com.example.xueyangli.ilovezappos.model.Product;
import com.example.xueyangli.ilovezappos.model.ScaleImageView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private Product product;
    private ScaleImageView productImageView;
    private ImageView smallProductImageView;
    private ImageButton addToCartButton;

    private KProgressHUD hud;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#7399C7"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        cart = ((MyApplication) this.getApplication()).getCart();

        product = (Product)getIntent().getSerializableExtra("product");

        ActivityProductBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_product);
        binding.setProduct(product);

        addToCartButton = (ImageButton)findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        doAnimation();
                        cart.add(product);
                        updateHotCount(cart.size());                    }
                }
        );

        /***************** Product ImageView Setup *****************/

        productImageView = (ScaleImageView)findViewById(R.id.product_image);

        smallProductImageView = new ImageView(this);
        RelativeLayout root = (RelativeLayout)findViewById(R.id.activity_product);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        smallProductImageView.setLayoutParams(layoutParams);
        smallProductImageView.setVisibility(View.INVISIBLE);
        root.addView(smallProductImageView);
        setContentView(root);

//        new LoadThumbnailTask().execute(product.thumbnailImageUrl);
        Picasso.with(this).load(product.thumbnailImageUrl).placeholder(R.mipmap.ic_launcher).into(productImageView);
        Picasso.with(this).load(product.thumbnailImageUrl).placeholder(R.mipmap.ic_launcher).into(smallProductImageView);

        /***************** Product ImageView Setup *****************/


        /***************** Price TextView Setup *****************/
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
        /***************** Price TextView Setup *****************/
    }

    private void doAnimation(){
        int[] location = new int[2];
        addToCartButton.getLocationOnScreen(location);
        smallProductImageView.setX(location[0] + 50);
        smallProductImageView.setY(location[1] - 300);

        smallProductImageView.setVisibility(View.VISIBLE);
        smallProductImageView.animate().rotationBy(-360).translationX(600).translationY(-150).setDuration(900);
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
