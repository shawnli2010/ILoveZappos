package com.example.xueyangli.ilovezappos;

import android.app.Application;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;

/**
 * Application class to declare global variables
 *
 * Created by xueyangli on 2/8/17.
 */

public class MyApplication extends Application {

    private ArrayList<Product> cart;

    public ArrayList<Product> getCart(){
        return cart;
    }

    public void setCart(ArrayList<Product> cart){
        this.cart = cart;
    }
}
