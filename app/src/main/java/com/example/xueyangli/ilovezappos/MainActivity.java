package com.example.xueyangli.ilovezappos;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.model.Product;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.example.xueyangli.ilovezappos.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity implements View.OnClickListener,Callback<ProductListResponse>{

    private Retrofit retrofit;

    private KProgressHUD hud;

    private Context mContext;

    private ArrayList<Product> productListDataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#7399C7"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        mContext = this;

        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        makeAPICall("nike");

        // Create the global cart object
        ArrayList<Product> global_cart = new ArrayList<Product>();
        ((MyApplication) this.getApplication()).setCart(global_cart);
        cart = ((MyApplication) this.getApplication()).getCart();

        // UI Setup
        final EditText search_bar = (EditText) findViewById(R.id.searchBar);
        Button search_button = (Button) findViewById(R.id.searchButton);

        search_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String user_input = search_bar.getText().toString();
                        makeAPICall(user_input);
                    }
                }
        );


    }

    @Override
    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
        hud.dismiss();
        String body = response.body().toString();
        boolean isSuccess = response.raw().isSuccessful();
        List<Product> products = new ArrayList<>();

        if(isSuccess){
            products = response.body().getProducts();
            productListDataSource = (ArrayList)products;
            Intent intent = new Intent(getBaseContext(), ProductListActivity.class);
            intent.putExtra("productListDataSource",productListDataSource);
            startActivity(intent);
        }
        else{
            showAlert("Please check your internet connection");
        }
    }

    @Override
    public void onFailure(Call<ProductListResponse> call, Throwable t) {
        hud.dismiss();
        showAlert("Please check your internet connection");
    }

    private void makeAPICall(String brand_name){
        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("LOADING")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        hud.show();

        ZapposAPI zapposAPI = retrofit.create(ZapposAPI.class);
        Call<ProductListResponse> call = zapposAPI.searchProducts(brand_name, ConstantUtil.API_KEY);

        //asynchronous call
        call.enqueue(this);
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

    private void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertLogin = builder.create();
        alertLogin.show();
    }

}
