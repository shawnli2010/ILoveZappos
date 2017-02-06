package com.example.xueyangli.ilovezappos;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

    public static final String SEARCH_URL = "https://api.zappos.com/Search";
    private static final String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Setup
        final EditText search_bar = (EditText) findViewById(R.id.searchBar);
        Button search_button = (Button) findViewById(R.id.searchButton);

        search_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String user_input = search_bar.getText().toString();
                        new QueryZapposAPITask().execute(SEARCH_URL,user_input,API_KEY);
                    }
                }
        );


    }

    class QueryZapposAPITask extends AsyncTask<String, Void, JSONObject> {

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

            try{
                JSONArray jArray = jsonObject.getJSONArray("results");

                if(jArray.length() == 0){
                    Log.d("EMPTY",String.format("No result found for input: %s",jsonObject.getString("originalTerm")));
                }

                for(int i = 0; i < jArray.length(); i++){
                    String product_name = jArray.getJSONObject(i).getString("productName");
                    Log.d(String.format("product %d",i),product_name);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
