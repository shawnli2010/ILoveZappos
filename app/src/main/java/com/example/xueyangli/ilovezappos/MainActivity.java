package com.example.xueyangli.ilovezappos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ProductData {
    ResponseData originalTerm;
}

class ResponseData {
    String translatedText;
}

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "https://api.zappos.com/";
    private static final String query_key = "b743e26728e16b81da139182bb2094357c31d331";

    private ZapposAPIService mService;

    public interface ZapposAPIService{
        @GET("/Search")
        Call<ProductData> getProduct(
                @Query("term") String brandname,
                @Query("key") String querykey);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Setup
        final EditText search_bar = (EditText) findViewById(R.id.searchBar);
        Button search_button = (Button)findViewById(R.id.searchButton);

        search_button.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Log.d("search_bar: ", search_bar.getText().toString());
                    }
                }
        );

        // Create Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        mService = retrofit.create(ZapposAPIService.class);

        Log.d("before", "mService.getProduct");
        mService.getProduct("nike",query_key).enqueue(new Callback<ProductData>() {
            @Override
            public void onResponse(Call<ProductData> call, Response<ProductData> response) {
                Log.d("in", "onResponse");
                if(response.body() == null){
                    Log.d("checkNull", "is null");
                }
                else{
                    Log.d("checkNull", "not null");
                }

            }

            @Override
            public void onFailure(Call<ProductData> call, Throwable t) {
                Log.d("in", "onFailure");
                Log.d("t",t.toString());
            }
        });
    }

//    public void translate(final String textToTranslate, final String fromLanguage, final String toLanguage) {
//        mService.getTranslation(textToTranslate, URLEncoder.encode(fromLanguage + "|" + toLanguage))
//                .enqueue(new Callback<TranslatedData>() {
//
//                    @Override
//                    public void onResponse(Response<TranslatedData> response, Retrofit retrofit) {
//                        String output =
//                                String.format("Translation of: %s, %s->%s = %s", textToTranslate,
//                                        fromLanguage, toLanguage, response.body().responseData.translatedText);
//
//                        System.out.println("Result: " + output);
//                    }
//
//                    @Override public void onFailure(Throwable t) {
//                        System.out.println("[DEBUG]" + " RestApi onFailure - " + "");
//                    }
//                });
//    }
}
