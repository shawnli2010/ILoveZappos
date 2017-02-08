package com.example.xueyangli.ilovezappos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String SEARCH_URL = "https://api.zappos.com/Search";
    private static final String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";
    private KProgressHUD hud;

    private Context mContext;

    private ArrayList<Product> productListDataSource;

    private ArrayList<Product> cart;
    private TextView currentCartSizeTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

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
                        new QueryZapposAPITask(mContext).execute(SEARCH_URL,user_input,API_KEY);
                    }
                }
        );
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

    class QueryZapposAPITask extends AsyncTask<String, Void, JSONObject> {

        Context context;

        public QueryZapposAPITask(Context mContext){
            this.context = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hud = KProgressHUD.create(MainActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("LOADING")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);

            hud.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String url = params[0];
            String brand_name = params[1];
            String api_key = params[2];

            String charset = "UTF-8";

            try {
                String query = String.format("term=%s&key=%s",
                        URLEncoder.encode(brand_name, charset),
                        URLEncoder.encode(api_key, charset));

                URLConnection connection = new URL(url + "?" + query).openConnection();
                connection.setRequestProperty("Accept-Charset", charset);
                InputStream response = connection.getInputStream();

                String res = "";
                try (Scanner scanner = new Scanner(response)) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    res += responseBody;
                }

                Log.d("query result", res);
                JSONObject result = new JSONObject(res);
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            hud.dismiss();

            if(jsonObject == null){
                showAlert("Please check your internet connection");
            }
            else{
                try{
                    JSONArray jArray = jsonObject.getJSONArray("results");

                    if(jArray.length() == 0) {
                        Log.d("EMPTY", String.format("No result found for input: %s", jsonObject.getString("originalTerm")));

                        showAlert("No result found for your input");
                    }
                    else{
                        productListDataSource = getProductList(jArray);
                        Intent intent = new Intent(getBaseContext(), ProductListActivity.class);
                        intent.putExtra("productListDataSource",productListDataSource);
                        startActivity(intent);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }

        private void showAlert(String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

        private ArrayList<Product> getProductList(JSONArray jArray){

            ArrayList<Product> result = new ArrayList<>();

            try{
                for(int i = 0; i < jArray.length(); i++) {
                    Product product = new Product();
                    product.brandName = jArray.getJSONObject(i).getString("brandName");
                    product.thumbnailImageUrl = jArray.getJSONObject(i).getString("thumbnailImageUrl");
                    product.productId = jArray.getJSONObject(i).getString("productId");
                    product.originalPrice = jArray.getJSONObject(i).getString("originalPrice");
                    product.styleId = jArray.getJSONObject(i).getString("styleId");
                    product.colorId = jArray.getJSONObject(i).getString("colorId");
                    product.price = jArray.getJSONObject(i).getString("price");
                    product.percentOff = jArray.getJSONObject(i).getString("percentOff");
                    product.productUrl = jArray.getJSONObject(i).getString("productUrl");
                    product.productName = jArray.getJSONObject(i).getString("productName");
                    result.add(product);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return result;

        }
    }
}
