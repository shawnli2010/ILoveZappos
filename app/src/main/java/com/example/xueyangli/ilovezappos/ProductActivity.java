package com.example.xueyangli.ilovezappos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.ImageView;

import com.example.xueyangli.ilovezappos.model.Product;
import com.example.xueyangli.ilovezappos.model.ScaleImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductActivity extends AppCompatActivity {

    private Product product;
    private ScaleImageView productImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (Product)getIntent().getSerializableExtra("product");
        productImageView = (ScaleImageView)findViewById(R.id.product_image);

        new LoadThumbnailTask().execute(product.thumbnailImageUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    class LoadThumbnailTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            String image_url = params[0] ;

            try{
                URL url = new URL(image_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);

            productImageView.setImageBitmap(bmp);
        }
    }
}
