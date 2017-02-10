package com.example.xueyangli.ilovezappos.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xueyangli.ilovezappos.ConstantUtil;
import com.example.xueyangli.ilovezappos.GeneralUtil;
import com.example.xueyangli.ilovezappos.MyApplication;
import com.example.xueyangli.ilovezappos.R;
import com.example.xueyangli.ilovezappos.model.Product;
import com.example.xueyangli.ilovezappos.model.ProductListResponse;
import com.example.xueyangli.ilovezappos.model.ZapposAPI;
import com.kaopiz.kprogresshud.KProgressHUD;

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

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    private GeneralUtil generalUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create util instance and set the actionbar's color
        generalUtil = new GeneralUtil(this);
        generalUtil.setActionBarColor();

        // Create the retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(ConstantUtil.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create and set the global_cart object
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

    /*
     * Retrofit2 call back method.
     * If success, it will start a ProductListActivity which lists out all the products that
     * are returned from the server.
     * Else it will show an alert dialog.
     */
    @Override
    public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
        hud.dismiss();

        boolean isSuccess = response.raw().isSuccessful();
        if(isSuccess){
            List<Product> products = response.body().getProducts();
            ArrayList<Product> productListDataSource = (ArrayList)products;

            Intent intent = new Intent(getBaseContext(), ProductListActivity.class);
            intent.putExtra("productListDataSource",productListDataSource);
            startActivity(intent);
        }
        else{
            generalUtil.showAlert("Please check your internet connection");
        }
    }

    /*
     * Retrofit2 call back method.
     * If the request failed, it will show an alert dialog to notify user to check their internet
     */
    @Override
    public void onFailure(Call<ProductListResponse> call, Throwable t) {
        hud.dismiss();
        generalUtil.showAlert("Please check your internet connection");
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
        generalUtil.updateHotCount(currentCartSizeTextView,cart.size());


        return true;
    }

    /**
     * makeAPICall will make a GET request to Zappos API using Retrofit2.
     * The returned JSON object will automatically be parsed to a List<Product>
     * that is encapsulated by ProductListResponse obejct
     *
     * @param brand_name the user-input string which represents the brand
     *                   that needs to be searched for
     */
    private void makeAPICall(String brand_name){
        // Show the progress hud while making the asynchronous call
        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("LOADING")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        hud.show();

        ZapposAPI zapposAPI = retrofit.create(ZapposAPI.class);
        Call<ProductListResponse> call = zapposAPI.searchProducts(brand_name, ConstantUtil.API_KEY);
        call.enqueue(this);
    }
}
