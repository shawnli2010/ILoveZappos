package com.example.xueyangli.ilovezappos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * General util class to define methods that will be shared by classes
 * Keep the project DRY
 *
 * Created by xueyangli on 2/9/17.
 */

public class GeneralUtil {

    private final Handler handler;
    private Context mContext;

    public GeneralUtil(Context context){
        mContext = context;
        handler = new Handler(context.getMainLooper());
    }

    /**
     * updateHotCount will update the red counter(shopping cart size indicator) on right
     * upper corner of action bar when passing in a new size of the shopping cart
     *
     * @param currentCartSizeTextView textview holds the string that represents
     *                                the size of the shopping cart
     * @param new_cart_size           the updated shopping cart size
     */
    public void updateHotCount(final TextView currentCartSizeTextView, final int new_cart_size) {
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

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    /**
     * showAlert will show an alert dialog with a message and an OK button
     *
     * @param message string represents the alert message
     */
    public void showAlert(String message){
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

    /**
     * setBarsColor will set the color of the action bar
     */
    public void setActionBarColor(){
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#7399C7"));
        AppCompatActivity appCompatActivity = (AppCompatActivity)mContext;
        appCompatActivity.getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }
}
